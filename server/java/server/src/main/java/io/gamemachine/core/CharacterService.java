package io.gamemachine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;
import io.gamemachine.chat.ChatMediator;
import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.BuildObjects;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.CharacterNotification;
import io.gamemachine.messages.CharacterUpdate;
import io.gamemachine.messages.Characters;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.ItemSlots;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;
import io.protostuff.ByteString;

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
		Character.db().deleteWhere("character_id = ?", characterId);
		sendNotification(playerId, characterId, "delete");
	}

	public void save(Character character) {
		if (!Character.db().save(character)) {
			throw new RuntimeException("Error saving Character " + character.id);
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
		character.zone = ZoneService.defaultZone();

		if (!Character.db().save(character)) {
			throw new RuntimeException("Error saving Character " + character.id);
		}

		sendNotification(playerId, characterId, "create");

		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		if (gameEntityManager != null) {
			gameEntityManager.OnCharacterCreated(character, data);
		}

		return character;
	}

	public void setItemSlots(String characterId, String slotsData) {
		Character character = find(characterId);
		character.setItemSlotData(slotsData);
		Character.db().save(character);
		
		byte[] bytes = Base64.decodeBase64(slotsData);
		ItemSlots itemSlots = ItemSlots.parseFrom(bytes);
		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		gameEntityManager.ItemSlotsUpdated(characterId, itemSlots);
		
		GameMessage gameMessage = new GameMessage();
		CharacterUpdate update = new CharacterUpdate();
		update.character = character;
		gameMessage.characterUpdate = update;
		PlayerMessage.broadcast(gameMessage, character.zone.name);
	}
	
	public void SetUmaData(Character character, String umaData) {
		character.setUmaData(umaData);
		Character.db().save(character);
	}

	public void setItemSlots(String characterId, ItemSlots itemSlots) {
		Character character = find(characterId);
		character.itemSlotData = Base64.encodeBase64String(itemSlots.toByteArray());
		Character.db().save(character);
	}
	
	public ItemSlots getItemSlots(String characterId) {
		Character character = find(characterId);
		
		if (Strings.isNullOrEmpty(character.itemSlotData)) {
			return new ItemSlots();
		} else {
			byte[] bytes = Base64.decodeBase64(character.itemSlotData);
			return ItemSlots.parseFrom(bytes);
		}
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
		if (characters.containsKey(characterId)) {
			return characters.get(characterId);
		} else {
			Character character = Character.db().findFirst("character_id = ?", characterId);
			if (character != null) {
				characters.put(characterId, character);
			}
			return character;
		}
	}

	public Character find(String playerId, String characterId) {
		if (characters.containsKey(characterId)) {
			return characters.get(characterId);
		} else {
			Character character = Character.db().findFirst("character_id = ?", characterId);
			if (character != null) {
				characters.put(characterId, character);
			}
			return character;
		}
	}

	public Character findFirst(String playerId) {
		List<Character> characters = Character.db().where("character_player_id = ?", playerId);
		return characters.isEmpty() ? null : characters.get(0);
	}

	public byte[] findPlayerCharacters(String playerId) {
		Characters characters = new Characters();
		characters.setCharactersList(Character.db().where("character_player_id = ?", playerId));
		return characters.toByteArray();
	}

	public static byte[] findAllCharacters() {
		Characters characters = new Characters();
		characters.setCharactersList(Character.db().findAll());
		return characters.toByteArray();
	}

}
