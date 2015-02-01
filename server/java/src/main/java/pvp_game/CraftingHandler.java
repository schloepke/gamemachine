package pvp_game;

import io.gamemachine.core.AuthToken;
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
			logger.warning("Add craftable index "+idx);
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
			reward.result = 1;
			reward.craftedId = item.id;
			reward.craftedQuantity = 1;
			removeItems(item);
			addItem(reward.craftedId, reward.craftedQuantity);
		} else {
			reward.result = 0;
			logger.warning("No match for "+idx);
		}

		return reward;
	}

	
	
	private void removeItems(CraftableItem craftable) {
		if (craftable.item1 != null) {
			removeItem(craftable.item1,craftable.item1_quantity);
		}

		if (craftable.item2 != null) {
			removeItem(craftable.item2,craftable.item2_quantity);
		}

		if (craftable.item3 != null) {
			removeItem(craftable.item3,craftable.item3_quantity);
		}

		if (craftable.item4 != null) {
			removeItem(craftable.item4,craftable.item4_quantity);
		}
	}
	
	private void removeItem(String id, int quantity) {
		RemovePlayerItem removePlayerItem = new RemovePlayerItem();
		removePlayerItem.id = id;
		removePlayerItem.quantity = quantity;
		GameMessage msg = new GameMessage();
		msg.playerId = playerId;
		msg.removePlayerItem = removePlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
	}
	
	private void addItem(String id, int quantity) {
		PlayerItem playerItem = new PlayerItem();
		playerItem.quantity = quantity;
		playerItem.id = id;

		AddPlayerItem addPlayerItem = new AddPlayerItem();
		addPlayerItem.playerItem = playerItem;
		GameMessage msg = new GameMessage();
		msg.playerId = playerId;
		msg.addPlayerItem = addPlayerItem;
		msg.authtoken = AuthToken.setToken();
		PlayerCommands.SendLocal(msg, "item_manager");
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub

	}

}
