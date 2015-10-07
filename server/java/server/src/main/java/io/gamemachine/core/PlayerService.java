package io.gamemachine.core;

import io.gamemachine.authentication.Authentication;
import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.OperationNotSupportedException;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;
import io.gamemachine.messages.Character;

public class PlayerService {

	public static String channel = "player_notifications";
	private int authType;
	public static int timeout = 2000;
	public static final int OBJECT_DB = 0;
	public static final int SQL_DB = 1;
	public Map<String, Player> players = new ConcurrentHashMap<String, Player>();
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

	private void sendNotification(String playerId, String action) {
		PlayerNotification playerNotification = new PlayerNotification();
		playerNotification.playerId = playerId;
		playerNotification.action = action;
		ActorRef ref = ChatMediator.getInstance().get(channel);
		ref.tell(new DistributedPubSubMediator.Publish(channel,playerNotification),null);
	}
	
	public void delete(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		
		CharacterService.instance().deleteForPlayer(player.id);
		if (authType == OBJECT_DB) {
			Player.store().delete(player.id);
		} else if (authType == SQL_DB) {
			Player.db().deleteWhere("player_id = ?", player.id);
		}
		
		players.remove(player.id);
		sendNotification(player.id,"delete");
	}
		
	public Boolean playerExists(String playerId, boolean quick) {
		if (quick) {
			if (players.containsKey(playerId)) {
				return true;
			} else {
				return false;
			}
		} else {
			Player player = find(playerId);
			if (player != null) {
				return true;
			}
			return false;
		}
	}
	
	public Player create(String playerId, String gameId) {
		return create(playerId,gameId,"player");
	}
	
	public Player create(String playerId, String gameId, String role) {
		Player player = find(playerId);
		if (player != null) {
			return player;
		}

		player = new Player();
		player.setId(playerId);
		player.setGameId(gameId);
		player.setRole(role);
		player.setAuthenticated(false);
		player.setAuthtoken(0);
		player.setIp(0);

		
		if (authType == OBJECT_DB) {
			CharacterService.instance().findPlayerCharacters(player.id);
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			if (!Player.db().save(player)) {
				throw new RuntimeException("Error saving Player " + playerId);
			}
		}

		players.put(playerId, player);
		sendNotification(player.id,"create");
		
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
				player = Player.store().get(playerId, timeout);
			} else if (authType == SQL_DB) {
				player = Player.db().findFirst("player_id = ?", playerId);
			}
			
			if (player != null) {
				players.put(playerId, player);
			}
			return player;
		}
	}
	
	public int getZone(String playerId) {
		return GameGrid.getPlayerZone(playerId);
	}
	
	public boolean playerIsAgent(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return false;
		} else {
			if (player.role.equals("agent_controller")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean playerHasRole(String playerId, String role) {
		Player player = find(playerId);
		if (player == null) {
			return false;
		} else {
			if (player.role.equals(role)) {
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
			return player.gameId;
		}
	}
	
	public boolean isAuthenticated(Player player) {
		return Authentication.isAuthenticated(player);
	}
	
	public boolean isAuthenticated(String playerId) {
		return Authentication.isAuthenticated(playerId);
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
		
		if (player.locked) {
			logger.info("Player " + playerId + " is locked");
			return false;
		}
		
		if (!Strings.isNullOrEmpty(player.passwordHash)) {
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
	
	public void setIp(String playerId, int ip) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		
		if (player.ip != 0 && player.ip != ip) {
			player.setIpChangedAt(System.currentTimeMillis());
		}
		
		player.setIp(ip);

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			Player.db().save(player);
		}
	}
	
	public void setCharacter(String playerId, String characterId) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		player.setCharacterId(characterId);

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			Player.db().save(player);
		}
	}
	
	public void setRole(String playerId, String role) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}
		player.setRole(role);

		if (authType == OBJECT_DB) {
			Player.store().set(player);
		} else if (authType == SQL_DB) {
			Player.db().save(player);
		}
	}
	
	public String getCharacter(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return null;
		} else {
			return player.characterId;
		}
	}
	
	public Player findByCharacterId(String characterId) {
		for (Player player : players.values()) {
			if (player.characterId.equals(characterId)) {
				return player;
			}
		}
		return null;
	}
	
	public void clearCache(String playerId) {
		if (players.containsKey(playerId)) {
			players.remove(playerId);
		}
	}
}
