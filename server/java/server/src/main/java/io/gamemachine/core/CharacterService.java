package io.gamemachine.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.CharacterNotification;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.PlayerNotification;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;

public class CharacterService {

	public static String channel = "character_notifications";
	private String globalUser = "global_user";
	private int authType;
	public static int timeout = 20;
	public static final int OBJECT_DB = 0;
	public static final int SQL_DB = 1;
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

	public static CharacterService getInstance() {
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
		ref.tell(new DistributedPubSubMediator.Publish(channel,characterNotification),null);
	}
	
	public void deleteForPlayer(String playerId) {
		if (authType == OBJECT_DB) {
			Characters.store().delete(playerId);
		} else if (authType == SQL_DB) {
			Character.db().deleteWhere("character_player_id = ?", playerId);
		}
		sendNotification(playerId,"all","delete");
	}

	public void delete(String playerId, String characterId) {
		if (authType == OBJECT_DB) {
			ObjectStoreHelper.delete(playerId, characterId);
			ObjectStoreHelper.delete(globalUser, characterId);
		} else if (authType == SQL_DB) {
			Character.db().deleteWhere("character_id = ?", characterId);
		}
		sendNotification(playerId,characterId,"delete");
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
	
	public Character create(String playerId, String id, String umaData) {
		Character global = find(globalUser, id);
		if (global != null) {
			return null;
		}

		Character character = new Character();
		character.setId(id);
		character.setPlayerId(playerId);
		character.setHealth(1000);
		character.setStamina(1000);
		character.setMagic(1000);
		character.setUmaData(umaData);

		if (authType == OBJECT_DB) {
			ObjectStoreHelper.update(character);
			global = character.clone();
			global.playerId = globalUser;
			ObjectStoreHelper.update(global);
		} else if (authType == SQL_DB) {
			if (!Character.db().save(character)) {
				throw new RuntimeException("Error saving Character " + character.id);
			}
		}
		sendNotification(playerId,id,"create");
		return character;
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
			return Character.db().findFirst("character_id = ?", characterId);
		} else {
			return null;
		}
	}
	
	public Character find(String playerId, String id) {
		if (authType == OBJECT_DB) {
			return ObjectStoreHelper.find(playerId, id);
		} else if (authType == SQL_DB) {
			return Character.db().findFirst("character_id = ?", id);
		} else {
			return null;
		}
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
