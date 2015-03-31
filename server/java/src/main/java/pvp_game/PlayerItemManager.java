package pvp_game;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.AuthToken;
import io.gamemachine.core.Commands;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.RemovePlayerItem;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.UseItem;
import io.gamemachine.messages.Character;

public class PlayerItemManager {

	public static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> > playerItems = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> >();
	public static ConcurrentHashMap<String, String> equippedItems = new ConcurrentHashMap<String, String>();
	
	public static PlayerItem playerItemGlobal(String id) {
		return playerItem(id, "global");

	}
	
	public static String getEquippedItem(String playerId) {
		Character character = CharacterHandler.currentCharacter(playerId);
		if (equippedItems.containsKey(character.id)) {
			return equippedItems.get(character.id);
		} else {
			return null;
		}
	}
	
	public static void handleUseItem(UseItem useItem, String playerId) {
		Character character = CharacterHandler.currentCharacter(playerId);
		if (StatusEffectHandler.skillEffects.containsKey(useItem.id)) {
			PlayerItem playerItem = PlayerItemManager.playerItem(useItem.id, character.id);
			if (useItem.action.equals("equip")) {
				equippedItems.put(character.id,useItem.id);
				StatusEffectHandler.setPassiveSkill(useItem.id, playerId, playerId,StatusEffectTarget.Action.Apply,StatusEffectTarget.PassiveFlag.ManualRemove);
			} else if (useItem.action.equals("unequip")) {
				StatusEffectHandler.setPassiveSkill(useItem.id, playerId, playerId,StatusEffectTarget.Action.Remove,StatusEffectTarget.PassiveFlag.NA);
			} else if (useItem.action.equals("consume")) {
				if (PlayerItemManager.hasPlayerItem(useItem.id, character.id)) {
					PlayerItemManager.removePlayerItem(useItem.id, character.id, 1);
					StatusEffectHandler.setPassiveSkill(useItem.id, playerId, playerId,StatusEffectTarget.Action.Apply,StatusEffectTarget.PassiveFlag.AutoRemove);
				}
			}
		}
	}
	
	public static boolean hasPlayerItem(String id, String characterId) {
		PlayerItem playerItem = playerItem(id,characterId);
		if (playerItem == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void removePlayerItemCache(String id, String characterId) {
		if (!playerItems.containsKey(characterId)) {
			playerItems.put(characterId, new ConcurrentHashMap<String, PlayerItem>());
		}
		ConcurrentHashMap<String, PlayerItem> items = playerItems.get(characterId);
		if (items.containsKey(id)) {
			items.remove(id);
		}
	}
	
	public static PlayerItem playerItem(String id, String characterId) {
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
	
		
	public static void seedCharacterItem(String id, int quantity) {
		for (io.gamemachine.messages.Character character : Commands.getCharacters()) {
			Player player = PlayerService.getInstance().find(character.playerId);
			if (player.role.equals("player")) {
				PlayerItem playerItem = playerItem(id,character.id);
				if (playerItem == null || playerItem.quantity < quantity) {
					removePlayerItemCache(id,character.id);
					addPlayerItem(character.playerId,id,quantity);
				}
			}
		}
	}
}
