package io.gamemachine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;
import io.gamemachine.chat.ChatMediator;
import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.CharacterNotification;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;

public class CharacterService {

	public static String channel = "character_notifications";
	private String globalUser = "global_user";
	private int authType;
	public static int timeout = 2000;
	public static final int OBJECT_DB = 0;
	public static final int SQL_DB = 1;

	public Map<String, Character> characters = new ConcurrentHashMap<String, Character>();

	private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

	private CharacterService() {
		String auth = AppConfig.Handlers.getAuth();
		if (auth.equals("io.gamemachine.authentication.DbAuthenticator")) {
			this.authType = 1;
		} else {
			this.authType = 0;
		}

		logger.info("CharacterService starting authType=" + authType);
	}

	private static class LazyHolder {
		private static final CharacterService INSTANCE = new CharacterService();
	}

	public static CharacterService instance() {
		return LazyHolder.INSTANCE;
	}

	public static class ObjectStoreHelper {
		public static boolean update(Character character) {
			Characters characters = getCharacters(character.playerId);
			List<Character> chars = characters.getCharactersList();
			for (int i = 0; i < chars.size(); i++) {
				Character c = chars.get(i);
				if (c.id.equals(character.id)) {
					chars.set(i, character);
					Characters.store().set(characters);
					return true;
				}
			}
			chars.add(character);
			Characters.store().set(characters);

			Character.store().set(character);
			return true;
		}

		public static boolean delete(String playerId, String id) {
			Characters characters = getCharacters(playerId);
			List<Character> chars = characters.getCharactersList();
			for (int i = 0; i < chars.size(); i++) {
				Character c = chars.get(i);
				if (c.id.equals(id)) {
					chars.remove(i);
					Characters.store().set(characters);
					return true;
				}
			}
			return false;
		}

		public static Character getCharacter(String characterId) {
			return Character.store().get(characterId, timeout);
		}

		public static Characters getCharacters(String playerId) {
			Characters characters = Characters.store().get(playerId, timeout);
			if (characters == null) {
				characters = new Characters();
				characters.id = playerId;
				Characters.store().set(characters);
			}
			return characters;
		}

		public static Character find(String playerId, String id) {
			Characters characters = getCharacters(playerId);
			List<Character> chars = characters.getCharactersList();
			for (int i = 0; i < chars.size(); i++) {
				Character c = chars.get(i);
				if (c.id.equals(id)) {
					return chars.get(i);
				}
			}
			return null;
		}
	}

	private void sendNotification(String playerId, String characterId, String action) {
		CharacterNotification characterNotification = new CharacterNotification();
		characterNotification.characterId = characterId;
		characterNotification.playerId = playerId;
		characterNotification.action = action;
		ActorRef ref = ChatMediator.getInstance().get(channel);
		ref.tell(new DistributedPubSubMediator.Publish(channel, characterNotification), null);
	}

	public void deleteForPlayer(String playerId) {
		if (authType == OBJECT_DB) {
			Characters.store().delete(playerId);
		} else if (authType == SQL_DB) {
			Character.db().deleteWhere("character_player_id = ?", playerId);
		}
		sendNotification(playerId, "all", "delete");
	}

	public void delete(String playerId, String characterId) {
		if (authType == OBJECT_DB) {
			ObjectStoreHelper.delete(playerId, characterId);
			ObjectStoreHelper.delete(globalUser, characterId + "_global");
		} else if (authType == SQL_DB) {
			Character.db().deleteWhere("character_id = ?", characterId);
		}
		sendNotification(playerId, characterId, "delete");
	}

	public void save(Character character) {
		if (authType == OBJECT_DB) {
			ObjectStoreHelper.update(character);
		} else if (authType == SQL_DB) {
			if (!Character.db().save(character)) {
				throw new RuntimeException("Error saving Character " + character.id);
			}
		}
	}

	public Character create(String playerId, String characterId, Vitals.Template template, Object data) {
		String globalCharacterId = characterId + "_global";
		Character global = find(globalUser, globalCharacterId);
		if (global != null) {
			return null;
		}

		Character character = new Character();
		character.vitalsTemplate = template;
		character.setId(characterId);
		character.setPlayerId(playerId);

		if (authType == OBJECT_DB) {
			ObjectStoreHelper.update(character);
			global = character.clone();
			global.playerId = globalUser;
			global.id = globalCharacterId;
			ObjectStoreHelper.update(global);
		} else if (authType == SQL_DB) {
			if (!Character.db().save(character)) {
				throw new RuntimeException("Error saving Character " + character.id);
			}
		}
		sendNotification(playerId, characterId, "create");

		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		if (gameEntityManager != null) {
			gameEntityManager.OnCharacterCreated(character, data);
		}

		return character;
	}

	public void SetUmaData(Character character, String umaData) {
		character.setUmaData(umaData);
		Character.db().save(character);
	}

	
	public Zone getZone(String characterId) {
		Character character = find(characterId);
		return character.zone;
	}

	public List<Character> search(String searchString) {
		List<Character> characters;

		if (authType == SQL_DB) {
			if (searchString.isEmpty()) {
				characters = Character.db().findAll();
			} else {
				characters = Character.db().where("character_id LIKE ?", "%" + searchString + "%");
			}

		} else {
			characters = new ArrayList<Character>();
		}
		return characters;
	}

	public Character find(String characterId) {
		if (authType == OBJECT_DB) {
			return ObjectStoreHelper.getCharacter(characterId);
		} else if (authType == SQL_DB) {
			if (characters.containsKey(characterId)) {
				return characters.get(characterId);
			} else {
				Character character = Character.db().findFirst("character_id = ?", characterId);
				if (character != null) {
					characters.put(characterId, character);
				}
				return character;
			}
		} else {
			return null;
		}
	}

	public Character find(String playerId, String characterId) {
		if (authType == OBJECT_DB) {
			return ObjectStoreHelper.find(playerId, characterId);
		} else if (authType == SQL_DB) {
			if (characters.containsKey(characterId)) {
				return characters.get(characterId);
			} else {
				Character character = Character.db().findFirst("character_id = ?", characterId);
				if (character != null) {
					characters.put(characterId, character);
				}
				return character;
			}

		} else {
			return null;
		}
	}

	public Character findFirst(String playerId) {
		List<Character> characters = Character.db().where("character_player_id = ?", playerId);
		return characters.isEmpty() ? null : characters.get(0);
	}
	
	public byte[] findPlayerCharacters(String playerId) {
		if (authType == OBJECT_DB) {
			return ObjectStoreHelper.getCharacters(playerId).toByteArray();
		} else if (authType == SQL_DB) {
			Characters characters = new Characters();
			characters.setCharactersList(Character.db().where("character_player_id = ?", playerId));
			return characters.toByteArray();
		} else {
			throw new RuntimeException("Invalid authType");
		}
	}

	public static byte[] findAllCharacters() {
		Characters characters = new Characters();
		characters.setCharactersList(Character.db().findAll());
		return characters.toByteArray();
	}

}
