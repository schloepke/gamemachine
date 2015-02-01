package io.gamemachine.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuthToken {

	private static ConcurrentHashMap<Long, Integer> tokens = new ConcurrentHashMap<Long, Integer>();
	private static AtomicLong counter = new AtomicLong();
	
	public static Boolean hasToken(Long token) {
		if (tokens.containsKey(token)) {
			tokens.remove(token);
			return true;
		} else {
			return false;
		}
	}
	
	public static Long setToken() {
		long token =  counter.incrementAndGet();
		tokens.put(token, 1);
		return token;
	}
}
