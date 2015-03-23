package pvp_game;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.AuthToken;
import io.gamemachine.core.Commands;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.RemovePlayerItem;

public class PlayerItemManager {

	public static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> > playerItems = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerItem> >();

	
	public static PlayerItem playerItemGlobal(String id) {
		return playerItem(id, "global");

	}

	public static boolean hasPlayerItem(String id, String characterId) {
		PlayerItem playerItem = playerItem(id,characterId);
		if (playerItem == null) {
			return false;
		} else {
			return true;
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
	}
	
		
	public static void seedCharacterItem(String id, int quantity) {
		for (io.gamemachine.messages.Character character : Commands.getCharacters()) {
			Player player = PlayerService.getInstance().find(character.playerId);
			if (player.role.equals("player")) {
				removePlayerItem(character.playerId,id,quantity);
				addPlayerItem(character.playerId,id,quantity);
			}
		}
	}
}
