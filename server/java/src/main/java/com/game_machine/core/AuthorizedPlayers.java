package com.game_machine.core;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorizedPlayers {
	public ConcurrentHashMap<String, Boolean> authorized = new ConcurrentHashMap<String, Boolean>();
	
	public void setAuthorized(String playerId) {
		authorized.put(playerId, true);
	}
	
	public void remove(String playerId) {
		authorized.remove(playerId);
	}
	
	public Boolean isAuthorized(String playerId) {
		if (authorized.containsKey(playerId)) {
			return true;
		} else {
			return false;
		}
		
	}
}
