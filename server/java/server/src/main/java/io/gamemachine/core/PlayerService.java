package io.gamemachine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;
import io.gamemachine.authentication.Authentication;
import io.gamemachine.chat.ChatMediator;
import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerNotification;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;

public class PlayerService {

	public static String channel = "player_notifications";
	private int authType;
	public static int timeout = 2000;
	public static final int OBJECT_DB = 0;
	public static final int SQL_DB = 1;
	public Map<String, Player> players = new ConcurrentHashMap<String, Player>();
	public Map<String, String> agentPasswords = new ConcurrentHashMap<String, String>();
	private Map<String, Zone> playerZone = new ConcurrentHashMap<String, Zone>();
	private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
	private int agentControllerCount = 50;

	private PlayerService() {
		String auth = AppConfig.Handlers.getAuth();
		if (auth.equals("io.gamemachine.authentication.DbAuthenticator")) {
			this.authType = 1;
		} else {
			this.authType = 0;
		}
		logger.info("PlayerService starting authType=" + authType);
	}

	private static class LazyHolder {
		private static final PlayerService INSTANCE = new PlayerService();
	}

	public static PlayerService getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getAgentPassword(String playerId) {
		return agentPasswords.get(playerId);
	}

	public List<Player> getAssignedAgents() {
		return Player.db().where("player_role = ? and player_assigned = ?", Player.Role.AgentController.getNumber(), true);
	}

	public synchronized Player assignAgent() {
		io.gamemachine.orm.models.Player.openTransaction();
		List<Player> agents = Player.db().where(true, "player_role = ? and player_assigned = ?", Player.Role.AgentController.getNumber(),
				false);
		Player agent = agents.isEmpty() ? null : agents.get(0);

		if (agent != null) {
			String password = UUID.randomUUID().toString();
			agent.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
			agent.setAssigned(true);
			agentPasswords.put(agent.id, password);
			
			List<Character> characters = Character.db().where(true,"character_player_id = ?", agent.id);
			Character character = characters.isEmpty() ? null : characters.get(0);
			agent.setCharacterId(character.id);
			
			Player.db().save(agent, true);
			players.put(agent.id, agent);
		}
		io.gamemachine.orm.models.Player.commitTransaction();
		return agent;
	}

	public boolean isAssignedToUnity(String playerId) {
		Player player = find(playerId);
		if (player == null || !player.assignedUnityInstance) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized Player assignUnityAgent() {
		io.gamemachine.orm.models.Player.openTransaction();
		List<Player> agents = Player.db().where(true,
				"player_role = ? and player_assigned = ? and player_assigned_unity_instance = ?", Player.Role.AgentController.getNumber(),
				true, false);
		
		Player agent = agents.isEmpty() ? null : agents.get(0);

		if (agent != null) {
			agent.setAssignedUnityInstance(true);
			Player.db().save(agent, true);
			players.put(agent.id, agent);
		}
		io.gamemachine.orm.models.Player.commitTransaction();
		return agent;
	}

	public synchronized void releastUnityAgent(String playerId) {
		Player agent = Player.db().findFirst("player_id = ?", playerId);

		if (agent != null) {
			agent.setAssignedUnityInstance(false);
			Zone zone = ZoneService.defaultZone();
			setZone(agent.id, zone);
			Player.db().save(agent);
			players.put(agent.id, agent);
		}
	}

	public void createAgentControllers() {
		CharacterService characterService = CharacterService.instance();
		String gameId = AppConfig.getDefaultGameId();
		String basename = "agent_";
		Zone zone = ZoneService.defaultZone();

		for (int i = 1; i < agentControllerCount; i++) {
			String playerId = basename + i;
			String characterId = basename + "c_" + i;
			Character character = characterService.find(playerId, characterId);

			Player player = find(playerId);
			if (player == null) {
				player = create(playerId, gameId);
			}

			if (character == null) {
				character = characterService.create(playerId, characterId, Vitals.Template.PlayerTemplate, null);
				characterService.save(character);
			}

			logout(playerId);
			setRole(playerId, Player.Role.AgentController);
			setPassword(playerId, UUID.randomUUID().toString());
			setCharacter(playerId, characterId);
			setZone(playerId, zone);
		}
	}

	public void createNpc(String playerId, String characterId, String prefab, Vitals.Template vitalsTemplate) {
		delete(playerId);
		
		CharacterService characterService = CharacterService.instance();
		String gameId = AppConfig.getDefaultGameId();
		Zone zone = ZoneService.defaultZone();

		Character character = characterService.find(playerId, characterId);

		Player player = find(playerId);
		if (player == null) {
			player = create(playerId, gameId);
		}

		if (character == null) {
			character = characterService.create(playerId, characterId, vitalsTemplate, null);
		}

		character.gameEntityPrefab = prefab;
		character.vitalsTemplate = vitalsTemplate;
		characterService.save(character);
		
		logout(playerId);
		setRole(playerId, Player.Role.Npc);
		setPassword(playerId, UUID.randomUUID().toString());
		setCharacter(playerId, characterId);
		setZone(playerId, zone);
	}
	
	public void sendNotification(String playerId, String action) {
		PlayerNotification playerNotification = new PlayerNotification();
		playerNotification.playerId = playerId;
		playerNotification.action = action;
		ActorRef ref = ChatMediator.getInstance().get(channel);
		ref.tell(new DistributedPubSubMediator.Publish(channel, playerNotification), null);
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
		sendNotification(player.id, "delete");
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
		return create(playerId, gameId, Player.Role.Player);
	}

	public Player create(String playerId, String gameId, Player.Role role) {
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
		sendNotification(player.id, "create");

		return player;
	}

	public List<Player> search(String searchString) {
		List<Player> players;

		if (authType == SQL_DB) {
			if (searchString.isEmpty()) {
				players = Player.db().findAll();
			} else {
				players = Player.db().where("player_id LIKE ?", "%" + searchString + "%");
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

	public void setZone(String playerId, Zone zone) {
		playerZone.put(playerId, zone);

		GridService.getInstance().removeEntityFromGrids(playerId);

		String characterId = getCharacterId(playerId);
		if (!Strings.isNullOrEmpty(characterId)) {
			Character character = CharacterService.instance().find(playerId, characterId);
			character.zone = zone;
			character.region = zone.region;
			CharacterService.instance().save(character);
		} else {
			logger.warn("Unable to find character id for entityId " + playerId);
		}
	}

	public Zone getZone(String playerId) {
		if (playerZone.containsKey(playerId)) {
			return playerZone.get(playerId);
		} else {
			Zone zone = ZoneService.defaultZone();
			String characterId = getCharacterId(playerId);
			if (!Strings.isNullOrEmpty(characterId)) {
				Character character = CharacterService.instance().find(playerId, characterId);
				if (character.zone != null) {
					zone = character.zone;
				}
				setZone(playerId, zone);
			}

			return zone;
		}
	}

	public boolean playerIsAgent(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return false;
		} else {
			if (player.role == Player.Role.AgentController) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean playerHasRole(String playerId, Player.Role role) {
		Player player = find(playerId);
		if (player == null) {
			return false;
		} else {
			if (player.role == role) {
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

	public int getAuthtoken(String playerId) {
		clearCache(playerId);
		Player player = find(playerId);
		if (player == null) {
			return 0;
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

	public boolean loggedIn(Player player) {
		if (player.authtoken == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void logout(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			logger.warn("Player " + playerId + " not found");
			return;
		}

		player.setAuthtoken(0);
		player.setCharacterId(null);
		player.setAssigned(false);
		player.setAssignedUnityInstance(false);
		Player.db().save(player);
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

	public void setRole(String playerId, Player.Role role) {
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

	public String getCharacterId(String playerId) {
		Player player = find(playerId);
		if (player == null) {
			return null;
		} else {
			return player.characterId;
		}
	}

	public Player findByCharacterId(String characterId) {
			Character character = CharacterService.instance().find(characterId);
			if (character == null) {
				return null;
			}
			return find(character.playerId);
	}

	public void clearCache(String playerId) {
		if (players.containsKey(playerId)) {
			players.remove(playerId);
		}
	}
}
