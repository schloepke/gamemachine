package pvp_game;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.AuthToken;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Harvest;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.TrackData.EntityType;

import java.util.List;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HarvestHandler extends GameMessageActor {

	public static String name = HarvestHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	public static long respawnTime = 60000 * 2;

	@Override
	public void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {

		if (exactlyOnce(gameMessage)) {
			Harvest harvest = gameMessage.harvest;
			Harvest existing = Harvest.store().get(harvest.id, 20);
			if (existing != null) {
				if (System.currentTimeMillis() - existing.harvestedAt < respawnTime) {
					harvest.result = 0;
					setReply(gameMessage);
					return;
				}
			}
			
			
			String itemId = nameToItemId(harvest.id);
			if (itemId == null) {
				harvest.result = 0;
			} else {
				harvest.result = 1;
				harvest.harvestedAt = System.currentTimeMillis();
				harvest.itemId = itemId;
				Harvest.store().set(harvest);
				addItem(harvest.itemId, 1);
			}
			setReply(gameMessage);
		}
	}

	private String nameToItemId(String name) {
		if (name.startsWith("hv_")) {
			return "iron_ore";
		} else {
			return null;
		}
	}
	
	private void broadcast(Harvest harvest) {
		Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default");
		List<TrackData> trackDatas = grid.getAll();

		if (trackDatas != null) {
			for (TrackData trackData : trackDatas) {
				if (trackData.entityType == EntityType.PLAYER) {
					GameMessage msg = new GameMessage();
					msg.harvest = harvest;
				}
			}
		}
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
