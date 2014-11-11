package io.gamemachine.authentication;

import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Player;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Authentication {

	private static ConcurrentHashMap<String, String> authenticatedUsers = new ConcurrentHashMap<String, String>();
	private static final Logger logger = LoggerFactory.getLogger(Authentication.class);

	private Authable playerAuth;

	public Authentication() {
		this.playerAuth = AuthorizedPlayers.getPlayerAuthentication();
	}

	public static boolean isAuthenticated(Player player) {
		if (authenticatedUsers.containsKey(player.getId())) {
			String authtoken = authenticatedUsers.get(player.getId());
			if (authtoken.equals(player.getAuthtoken())) {
				return true;
			} else {
				// Reload from database to see if it's been updated
				authtoken = PlayerService.getInstance().getAuthtoken(player.getId());
				if (authtoken.equals(player.getAuthtoken())) {
					authenticatedUsers.put(player.getId(),authtoken);
					return true;
				}
			}
		}
		return false;
	}

	public static void unregisterPlayer(String playerId) {
		authenticatedUsers.remove(playerId);
	}

	public void registerPlayer(Player player) {
		authenticatedUsers.put(player.getId(), authtokenForPlayer(player));
	}

	public String authtokenForPlayer(Player player) {
		if (isPublic()) {
			return player.getAuthtoken();
		} else {
			return PlayerService.getInstance().getAuthtoken(player.getId());
		}
	}

	public boolean isPublic() {
		return playerAuth.isPublic();
	}

	public boolean authtokenIsValid(Player player) {
		if (isPublic() || player.authtoken.equals(authtokenForPlayer(player))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean authenticate(Player player) {
		if (authtokenIsValid(player)) {
			registerPlayer(player);
			player.setAuthenticated(true);
			return true;
		} else {
			logger.warn("Authentication for " + player.getId() + " failed");
			return false;
		}
	}

}
