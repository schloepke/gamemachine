package com.game_machine.config;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;

import com.game_machine.core.PlayerService;
import com.google.common.util.concurrent.RateLimiter;

public class GameLimits {

	private static final Logger logger = LoggerFactory.getLogger(GameLimits.class);
	
	private static ConcurrentHashMap<String, RateLimiter> messageLimiters = new ConcurrentHashMap<String, RateLimiter>();
		
	public static class RateLimitExceeded extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public RateLimitExceeded(String message){
		      super(message);
		   }
	}
	
	public static void resetMessageLimit(String gameId) {
		if (messageLimiters.containsKey(gameId)) {
			messageLimiters.remove(gameId);
		}
	}
	
	public static boolean messageLimitExceeded(ClientMessage clientMessage) {
		String gameId = PlayerService.getInstance().getGameId(clientMessage.getPlayer().getId());
		if (gameId == null) {
			return true;
		}
		if (checkLimit(gameId)) {
			return false;
		} else {
			logger.info("Rate limit exceeded for "+gameId);
			return true;
		}
	}
	
	public static RateLimiter getLimiter(String gameId) {
		if (!messageLimiters.containsKey(gameId)) {
			int messageLimit = GameConfig.getMessageLimit(gameId);
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
