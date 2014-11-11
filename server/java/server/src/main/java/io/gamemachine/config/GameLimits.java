package io.gamemachine.config;

import io.gamemachine.core.PlayerService;
import io.gamemachine.core.UserApi;
import io.gamemachine.core.UserApi.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.ClientMessage;
import akka.actor.UntypedActor;

import com.google.common.util.concurrent.RateLimiter;

public class GameLimits extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(GameLimits.class);

	private static long messagesIn = 0;
	private static long messagesOut = 0;
	
	private static Map<String, Integer> messageLimits = new ConcurrentHashMap<String, Integer>();
	private static Map<String, Integer> connectionLimits = new ConcurrentHashMap<String, Integer>();
	private static Map<String, AtomicInteger> messageCountsIn = new ConcurrentHashMap<String, AtomicInteger>();
	private static Map<String, AtomicInteger> messageCountsOut = new ConcurrentHashMap<String, AtomicInteger>();
	private static Map<String, AtomicInteger> connectionCounts = new ConcurrentHashMap<String, AtomicInteger>();
	
	private static Map<String, AtomicInteger> bytesTransferred = new ConcurrentHashMap<String, AtomicInteger>();
	
	private static ConcurrentHashMap<String, RateLimiter> messageLimiters = new ConcurrentHashMap<String, RateLimiter>();

	private UserApi userApi;

	public GameLimits() {
		String username = AppConfig.Gamecloud.getUser();
		String apiKey = AppConfig.Gamecloud.getApiKey();
		String hostname = AppConfig.Gamecloud.getHost();
		userApi = new UserApi(hostname, username, apiKey);
	}

	private void updateUserLimits() {
		for (String gameId : messageCountsIn.keySet()) {
			int messageCountIn = getMessageCountIn(gameId);
			int messageCountOut = getMessageCountOut(gameId);
			int messageCount = messageCountIn + messageCountOut;
			int transferred = getBytesTransferred(gameId);
			int connectionCount = getConnectionCount(gameId);
			Response response = userApi.updateCounts(gameId, messageCount, connectionCount, transferred);
			if (response.status == 200) {
				Integer messageLimit = response.params.get("message_limit").asInt();
				messageLimits.put(gameId, messageLimit);
				Integer connectionLimit = response.params.get("connection_limit").asInt();
				connectionLimits.put(gameId, connectionLimit);
				
				messagesIn = messageCountIn;
				messagesOut = messageCountOut;
				resetMessageCountOut(gameId);
				resetMessageCountIn(gameId);
				resetBytesTransferred(gameId);
			} else {
				logger.info("status:"+response.status + " "+response.body);
			}
		}
	}

	@Override
	public void preStart() {
		tick(10000, "tick");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
			if (msg.equals("tick")) {
				updateUserLimits();
				tick(10000, "tick");
			}
		}

	}

	public void tick(int delay, Object message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

	public static class RateLimitExceeded extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public RateLimitExceeded(String message) {
			super(message);
		}
	}

	public static int getBytesTransferred(String gameId) {
		AtomicInteger counter = bytesTransferred.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			bytesTransferred.put(gameId, counter);
		}
		return counter.get();
	}
	
	public static void addBytesTransferred(String gameId, int size) {
		AtomicInteger counter = bytesTransferred.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			bytesTransferred.put(gameId, counter);
		}
		counter.addAndGet(size);
	}
	
	public static void resetBytesTransferred(String gameId) {
		AtomicInteger counter = bytesTransferred.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			bytesTransferred.put(gameId, counter);
		}
		counter.set(0);
	}
	
	public static boolean isConnectionLimitReached(String gameId) {
		Integer connectionLimit = connectionLimits.get(gameId);
		if (connectionLimit == null) {
			return false;
		}
		
		AtomicInteger counter = connectionCounts.get(gameId);
		
		if (counter == null) {
			return false;
		}
		
		if (counter.get() >= connectionLimit) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void incrementConnectionCount(String gameId) {
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		}
		counter.incrementAndGet();
	}

	public static void decrementConnectionCount(String gameId) {
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		} else if (counter.get() >= 1){
			counter.decrementAndGet();
		}
	}

	public static void resetConnectionCount(String gameId) {
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		} else {
			counter.set(0);
		}
	}

	public static int getConnectionCount(String gameId) {
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		}
		return counter.get();
	}

	public static void incrementMessageCountIn(String gameId) {
		AtomicInteger counter = messageCountsIn.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsIn.put(gameId, counter);
		}
		counter.incrementAndGet();
	}
	
	public static long getMpsOut() {
		return messagesOut / 10l;
	}
	
	public static long getMpsIn() {
		return messagesIn / 10l;
	}
		
	public static void resetMessageCountIn(String gameId) {
		AtomicInteger counter = messageCountsIn.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsIn.put(gameId, counter);
		} else {
			counter.set(0);
		}
	}

	public static int getMessageCountIn(String gameId) {
		AtomicInteger counter = messageCountsIn.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsIn.put(gameId, counter);
		}
		return counter.get();
	}
	
	public static void incrementMessageCountOut(String gameId) {
		AtomicInteger counter = messageCountsOut.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsOut.put(gameId, counter);
		}
		counter.incrementAndGet();
	}

	public static void resetMessageCountOut(String gameId) {
		AtomicInteger counter = messageCountsOut.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsOut.put(gameId, counter);
		} else {
			counter.set(0);
		}
	}

	public static int getMessageCountOut(String gameId) {
		AtomicInteger counter = messageCountsOut.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsOut.put(gameId, counter);
		}
		return counter.get();
	}

	public static boolean messageLimitExceeded(ClientMessage clientMessage) {
		String gameId = PlayerService.getInstance().getGameId(clientMessage.getPlayer().getId());
		if (gameId == null) {
			return true;
		}
		if (checkLimit(gameId)) {
			return false;
		} else {
			logger.info("Rate limit exceeded for " + gameId);
			return true;
		}
	}

	public static RateLimiter getLimiter(String gameId) {
		if (!messageLimiters.containsKey(gameId)) {
			Integer messageLimit = messageLimits.get(gameId);
			if (messageLimit == null) {
				messageLimit = 500;
				messageLimits.put(gameId, messageLimit);
			}
			messageLimiters.put(gameId, RateLimiter.create(messageLimit));
		}
		return messageLimiters.get(gameId);
	}

	public static boolean checkLimit(String gameId) {
		RateLimiter limiter = getLimiter(gameId);
		if (limiter.tryAcquire()) {
			return true;
		} else {
			return false;
		}
	}

}
