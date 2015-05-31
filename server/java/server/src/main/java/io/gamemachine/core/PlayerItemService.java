package io.gamemachine.core;

import java.util.concurrent.ConcurrentHashMap;

import plugins.pvp_game.CharacterHandler;
import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.RemovePlayerItem;

public class PlayerItemService {

	public static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> > playerItems = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> >();
	public static ConcurrentHashMap<String, String> equippedItems = new ConcurrentHashMap<String, String>();
	
	public static PlayerItem playerItemGlobal(String id) {
		return getPlayerItem(id, "global");

	}
	
	public static String getEquippedItem(String playerId) {
		Character character = CharacterHandler.currentCharacter(playerId);
		if (equippedItems.containsKey(character.id)) {
			return equippedItems.get(character.id);
		} else {
			return null;
		}
	}
	
	public static void setEquippedItem(String characterId, String itemId) {
		equippedItems.put(characterId,itemId);
	}
	
	public static boolean hasPlayerItem(String id, String characterId) {
		PlayerItem playerItem = getPlayerItem(id,characterId);
		if (playerItem == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void removePlayerItemCache(String id, String characterId) {
		if (equippedItems.contains(characterId)) {
			equippedItems.remove(characterId);
		}
		
		if (!playerItems.containsKey(characterId)) {
			playerItems.put(characterId, new ConcurrentHashMap<String, PlayerItem>());
		}
		ConcurrentHashMap<String, PlayerItem> items = playerItems.get(characterId);
		if (items.containsKey(id)) {
			items.remove(id);
		}
	}
	
	public static PlayerItem getPlayerItem(String id, String characterId) {
		if (!playerItems.containsKey(characterId)) {
			playerItems.put(characterId, new ConcurrentHashMap<String, PlayerItem>());
		}
		ConcurrentHashMap<String, PlayerItem> items = playerItems.get(characterId);
		if (items.containsKey(id)) {
			return items.get(id);
		} else {
			PlayerItem playerItem = PlayerItem.db().findFirst("player_item_id = ? and player_item_player_id = ?", id, characterId);
			if (playerItem == null) {
				return null;
			} else {
				items.put(id, playerItem);
				return playerItem;
			}
		}
	}
	
	public static void addPlayerItem(String characterId, String id, int quantity) {
		PlayerItem playerItem = new PlayerItem();
		playerItem.quantity = quantity;
		playerItem.id = id;

		AddPlayerItem addPlayerItem = new AddPlayerItem();
		addPlayerItem.playerItem = playerItem;
		GameMessage msg = new GameMessage();
		msg.playerId = characterId;
		msg.addPlayerItem = addPlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
		removePlayerItemCache(id,characterId);
	}
	
	public static void removePlayerItem(String characterId, String id, int quantity) {
		RemovePlayerItem removePlayerItem = new RemovePlayerItem();
		removePlayerItem.id = id;
		removePlayerItem.quantity = quantity;
		GameMessage msg = new GameMessage();
		msg.playerId = characterId;
		msg.removePlayerItem = removePlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
		removePlayerItemCache(id,characterId);
	}
}
