package plugins.pvp_game;

import io.gamemachine.core.AuthToken;
import io.gamemachine.core.Commands;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.CraftItem;
import io.gamemachine.messages.CraftableItem;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.RemovePlayerItem;

import java.util.HashMap;
import java.util.Map;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CraftingHandler extends GameMessageActor {

	public static String name = CraftingHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	public static long respawnTime = 60000 * 2;
	public Map<String, CraftableItem> craftableItems = new HashMap<String, CraftableItem>();

	@Override
	public void awake() {
		loadCraftables();
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {

		if (exactlyOnce(gameMessage)) {
			gameMessage.craftItem = craft(gameMessage.craftItem);
			setReply(gameMessage);
		}
	}

	private void loadCraftables() {
		for (CraftableItem craftable : CraftableItem.db().findAll()) {
			String idx = ciIndex(craftable);
			craftableItems.put(idx, craftable);
			logger.warning("Add craftable index " + idx);
		}
	}

	private String ciIndex(CraftableItem craftable) {
		String idx = "";
		if (craftable.item1 != null) {
			idx += craftable.item1 + ":" + craftable.item1_quantity + ",";
		}

		if (craftable.item2 != null) {
			idx += craftable.item2 + ":" + craftable.item2_quantity + ",";
		}

		if (craftable.item3 != null) {
			idx += craftable.item3 + ":" + craftable.item3_quantity + ",";
		}

		if (craftable.item4 != null) {
			idx += craftable.item4 + ":" + craftable.item4_quantity;
		}
		return idx;
	}

	private CraftItem craft(CraftItem craftItem) {
		String idx = ciIndex(craftItem.craftableItem);

		CraftItem reward = new CraftItem();
		if (craftableItems.containsKey(idx)) {
			CraftableItem item = craftableItems.get(idx);
			if (hasItems(item)) {
				reward.result = 1;
				reward.craftedId = item.id;
				reward.craftedQuantity = 1;
				removeItems(item);
				addItem(reward.craftedId, reward.craftedQuantity);
			} else {
				reward.result = 0;
				logger.warning("Insufficient resources to craft " + idx);
			}
			
		} else {
			reward.result = 0;
			logger.warning("No match for " + idx);
		}

		return reward;
	}

	private Boolean hasItems(CraftableItem craftable) {

		if (craftable.item1 != null) {
			if (!hasItem(craftable.item1, craftable.item1_quantity)) {
				return false;
			}
		}

		if (craftable.item2 != null) {
			if (!hasItem(craftable.item2, craftable.item2_quantity)) {
				return false;
			}
		}

		if (craftable.item3 != null) {
			if (!hasItem(craftable.item3, craftable.item3_quantity)) {
				return false;
			}
		}

		if (craftable.item4 != null) {
			if (!hasItem(craftable.item4, craftable.item4_quantity)) {
				return false;
			}
		}
		return true;
	}

	private void removeItems(CraftableItem craftable) {

		if (craftable.item1 != null) {
			removeItem(craftable.item1, craftable.item1_quantity);
		}

		if (craftable.item2 != null) {
			removeItem(craftable.item2, craftable.item2_quantity);
		}

		if (craftable.item3 != null) {
			removeItem(craftable.item3, craftable.item3_quantity);
		}

		if (craftable.item4 != null) {
			removeItem(craftable.item4, craftable.item4_quantity);
		}
	}

	private Boolean hasItem(String id, int quantity) {
		PlayerItem existing = PlayerItem.db().findFirst("player_item_player_id = ? AND player_item_id = ?",
				characterId, id);
		if (existing != null && existing.quantity >= quantity) {
			return true;
		} else {
			return false;
		}
	}

	private void removeItem(String id, int quantity) {
		PlayerItemManager.removePlayerItem(playerId, id, quantity);
	}

	private void addItem(String id, int quantity) {
		PlayerItemManager.addPlayerItem(playerId, id, quantity);
	}

}
