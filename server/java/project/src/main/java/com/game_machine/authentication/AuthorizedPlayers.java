package com.game_machine.authentication;

import java.util.concurrent.ConcurrentHashMap;

public class AuthorizedPlayers {
	public static ConcurrentHashMap<String, Boolean> authorized = new ConcurrentHashMap<String, Boolean>();
	private static Authable playerAuthentication;
	
	public static void setAuthorized(String playerId) {
		authorized.put(playerId, true);
	}

	public static void remove(String playerId) {
		authorized.remove(playerId);
	}

	public static Boolean isAuthorized(String playerId) {
		if (authorized.containsKey(playerId)) {
			return true;
		} else {
			return false;
		}

	}

	public static Authable getPlayerAuthentication() {
		return playerAuthentication;
	}

	public static void setPlayerAuthentication(Authable playerAuthentication) {
		AuthorizedPlayers.playerAuthentication = playerAuthentication;
	}
}
