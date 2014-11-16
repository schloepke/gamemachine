package io.gamemachine.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerService {

	private int authType;
	public static final int OBJECT_DB = 0;
	public static final int SQL_DB = 1;
	public ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>();
	private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
	
	private PlayerService() {
		String auth = AppConfig.Handlers.getAuth();
		if (auth.equals("io.gamemachine.authentication.DbAuthenticator")) {
			this.authType = 1;
		} else {
			this.authType = 0;
		}
		logger.info("PlayerService starting authType="+authType);
	}

	private static class LazyHolder {
		private static final PlayerService INSTANCE = new PlayerService();
	}

	public static PlayerService getInstance() {
		return LazyHolder.INSTANCE;
	}

	public void delete(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		
		if (authType == OBJECT_DB) {
			Player.store().delete(player.id);;
		} else if (authType == SQL_DB) {
			Player.db().deleteWhere("player_id = ?", player.id);
		}
		
		players.remove(player.id);
	}
	
	public Player create(String playerId, String gameId) {
		Player player = find(playerId);
		if (player != null) {
			throw new RuntimeException("Player " + playerId + " exists");
		}

		player = new Player();
		player.setId(playerId);
		player.setGameId(gameId);

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			if (!Player.db().save(player)) {
				throw new RuntimeException("Error saving Player " + playerId);
			}
		}

		players.put(playerId, player);

		return player;
	}

	public List<Player> search(String searchString) {
		List<Player> players;
	
		if (authType == SQL_DB) {
			if (searchString.isEmpty()) {
				players = Player.db().findAll();
			} else {
				players = Player.db().where("player_id LIKE ?", "%"+searchString+"%");
			}
			
		} else {
			players = new ArrayList<Player>();
		}
		return players;
	}
	
	public Player find(String playerId) {
		
		if (players.containsKey(playerId)) {
			return players.get(playerId);
		} else {
			
			Player player = null;
			if (authType == OBJECT_DB) {
				player = Player.store().get(playerId, 1000);
			} else if (authType == SQL_DB) {
				player = Player.db().findFirst("player_id = ?", playerId);
			}
			
			if (player != null) {
				players.put(playerId, player);
			}
			return player;
		}
	}

	public boolean playerIsAgent(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return false;
		} else {
			if (player.getRole().equals("agent_controller")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public String getGameId(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return null;
		} else {
			return player.getGameId();
		}
	}
	
	public Integer getAuthtoken(String playerId) {
		clearCache(playerId);
		Player player = find(playerId);
		if (player == null) {
			return null;
		} else {
			return player.authtoken;
		}
	}
	
	
	public boolean authenticate(String playerId, String password) {
		clearCache(playerId);
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return false;
		}
		
		if (player.hasLocked() && player.locked) {
			logger.info("Player " + playerId + " is locked");
			return false;
		}
		
		if (player.hasPasswordHash()) {
			return BCrypt.checkpw(password, player.getPasswordHash());
		} else {
			return false;
		}
	}

	public void setPassword(String playerId, String password) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		
		player.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			Player.db().save(player);
		}
	}
		
	public void setAuthtoken(String playerId, int authtoken) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		
		player.setAuthtoken(authtoken);

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			Player.db().save(player);
		}
	}
	
	public void clearCache(String playerId) {
		if (players.containsKey(playerId)) {
			players.remove(playerId);
		}
	}
}
