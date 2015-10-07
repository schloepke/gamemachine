package io.gamemachine.config;

import io.gamemachine.core.PlayerService;
import io.gamemachine.core.UserApi;
import io.gamemachine.core.UserApi.Response;
import io.gamemachine.messages.ClientMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.RateLimiter;

public class GameLimits extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(GameLimits.class);

	private static long currentMessagesIn = 0;
	private static long currentMessagesOut = 0;
	private static long currentBytesOut = 0;
	private static long currentMessagesInOut = 0;
	private static int currentConnectionCount = 0;

	private static Map<String, Integer> playerBytesOut = new ConcurrentHashMap<String, Integer>();
	
	private static Map<String, Integer> messageLimits = new ConcurrentHashMap<String, Integer>();
	private static Map<String, Integer> connectionLimits = new ConcurrentHashMap<String, Integer>();
	private static Map<String, AtomicInteger> messageCountsIn = new ConcurrentHashMap<String, AtomicInteger>();
	private static Map<String, AtomicInteger> messageCountsOut = new ConcurrentHashMap<String, AtomicInteger>();
	private static Map<String, AtomicInteger> connectionCounts = new ConcurrentHashMap<String, AtomicInteger>();

	private static Map<String, AtomicInteger> bytesTransferred = new ConcurrentHashMap<String, AtomicInteger>();
	private static Map<String, AtomicInteger> playerBytesTransferred = new ConcurrentHashMap<String, AtomicInteger>();
	
	private static ConcurrentHashMap<String, RateLimiter> messageLimiters = new ConcurrentHashMap<String, RateLimiter>();

	private UserApi userApi;

	public GameLimits() {
		String username = AppConfig.Gamecloud.getUser();
		String apiKey = AppConfig.Gamecloud.getApiKey();
		String hostname = AppConfig.Gamecloud.getHost();
		//userApi = new UserApi(hostname, username, apiKey);
	}

	private void updateUserLimits() {
		currentMessagesIn = 0;
		currentMessagesOut = 0;
		currentBytesOut = 0;
		for (String gameId : messageCountsIn.keySet()) {
			currentMessagesIn += getMessageCountIn(gameId);
			currentMessagesOut += getMessageCountOut(gameId);
			currentBytesOut += getBytesTransferred(gameId);
			
		}

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

				resetMessageCountOut(gameId);
				resetMessageCountIn(gameId);
				resetBytesTransferred(gameId);
			} else {
				logger.info("status:" + response.status + " " + response.body);
			}
		}
	}

	private void updateStats() {
		currentMessagesIn = 0;
		currentMessagesOut = 0;
		currentBytesOut = 0;
		currentMessagesInOut = 0;
		currentConnectionCount = 0;
		
		for (String gameId : messageCountsIn.keySet()) {
			currentMessagesIn += getMessageCountIn(gameId);
			currentMessagesOut += getMessageCountOut(gameId);
			currentBytesOut += getBytesTransferred(gameId);
			currentMessagesInOut += (currentMessagesIn + currentMessagesOut);
			currentConnectionCount += getConnectionCount(gameId);
		}

		for (String gameId : messageCountsIn.keySet()) {
			int messageCountIn = getMessageCountIn(gameId);
			int messageCountOut = getMessageCountOut(gameId);
			int messageCount = messageCountIn + messageCountOut;
			int transferred = getBytesTransferred(gameId);
			int connectionCount = getConnectionCount(gameId);
			resetMessageCountOut(gameId);
			resetMessageCountIn(gameId);
			resetBytesTransferred(gameId);
		}
		
		for (String playerId : PlayerService.getInstance().players.keySet()) {
			int bytesOut = getPlayerBytesTransferred(playerId);
			setPlayerCurrentBytes(playerId,bytesOut);
			resetPlayerBytesTransferred(playerId);
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
				updateStats();
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

	public static AtomicInteger getPlayerByteCounter(String playerId) {
		AtomicInteger playerCounter = playerBytesTransferred.get(playerId);
		if (playerCounter == null) {
			playerCounter = new AtomicInteger();
			playerBytesTransferred.put(playerId, playerCounter);
		}
		return playerCounter;
	}
	
	public static AtomicInteger getByteCounter(String gameId) {
		AtomicInteger counter = bytesTransferred.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			bytesTransferred.put(gameId, counter);
		}
		return counter;
	}
	
	public static int getPlayerBytesTransferred(String playerId) {
		AtomicInteger counter = getPlayerByteCounter(playerId);
		return counter.get();
	}
	
	public static int getBytesTransferred(String gameId) {
		AtomicInteger counter = getByteCounter(gameId);
		return counter.get();
	}

	public static void addBytesTransferred(String gameId, String playerId,int size) {
		AtomicInteger counter = getByteCounter(gameId);
		counter.addAndGet(size);
		
		AtomicInteger playerCounter = getPlayerByteCounter(playerId);
		playerCounter.addAndGet(size);
	}

	public static void resetBytesTransferred(String gameId) {
		AtomicInteger counter = getByteCounter(gameId);
		counter.set(0);
	}

	public static void resetPlayerBytesTransferred(String playerId) {
		AtomicInteger playerCounter = getPlayerByteCounter(playerId);
		playerCounter.set(0);
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
		} else if (counter.get() >= 1) {
			counter.decrementAndGet();
		}
	}

	public static void resetConnectionCount(String gameId) {
		if (Strings.isNullOrEmpty(gameId)) {
			return;
		}
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		} else {
			counter.set(0);
		}
	}

	public static int getConnectionCount() {
		return currentConnectionCount;
	}
	
	public static int getConnectionCount(String gameId) {
		if (Strings.isNullOrEmpty(gameId)) {
			return 0;
		}
		AtomicInteger counter = connectionCounts.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			connectionCounts.put(gameId, counter);
		}
		return counter.get();
	}

	public static void incrementMessageCountIn(String gameId) {
		if (Strings.isNullOrEmpty(gameId)) {
			return;
		}
		AtomicInteger counter = messageCountsIn.get(gameId);
		if (counter == null) {
			counter = new AtomicInteger();
			messageCountsIn.put(gameId, counter);
		}
		counter.incrementAndGet();
	}

	public static int getPlayerCurrentBytes(String playerId) {
		if (playerBytesOut.containsKey(playerId)) {
			return playerBytesOut.get(playerId);
		} else {
			return 0;
		}
	}
	
	public static void setPlayerCurrentBytes(String playerId, int bytes) {
		playerBytesOut.put(playerId, bytes);
	}
	
	public static long getPlayerBpsOut(String playerId) {
		long bytes = getPlayerCurrentBytes(playerId);
		return bytes / 10l;
	}
	
	public static long getBytesPerMessageOut() {
		return getBpsOut()/getMpsOut();
	}
	
	public static long getMpsInOut() {
		return currentMessagesInOut / 10l;
	}
	
	public static long getMpsOut() {
		return currentMessagesOut / 10l;
	}

	public static long getMpsIn() {
		return currentMessagesIn / 10l;
	}

	public static long getBpsOut() {
		return currentBytesOut / 10l;
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
