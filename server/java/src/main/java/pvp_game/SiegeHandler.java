package pvp_game;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.WorldObject;

public class SiegeHandler extends GameMessageActor {

	public static String name = SiegeHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasSiegeCommand()) {
			if (gameMessage.siegeCommand.hasHitId()) {
				doHit(gameMessage.siegeCommand.hitId,gameMessage.siegeCommand.skillId);
			} else {
				Grid grid = GameGrid.getGameGrid("mygame", "default", playerId);
				for (TrackData trackData : grid.getAll()) {
					PlayerCommands.sendGameMessage(gameMessage, trackData.id);
				}
			}
			
		}

	}

	private void doHit(String id, String skillId) {
		WorldObject worldObject = WorldObject.db().findFirst("world_object_id = ?", id);
		if (worldObject != null) {
			if (!worldObject.hasHealth()) {
				worldObject.maxHealth = 10000;
				worldObject.health = 10000;
			}
			worldObject.health -= 1000;
			WorldObject.db().save(worldObject);
			ConsumableItemHandler.wobjects.put(worldObject.id, worldObject);
		}
	}
	
	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub
		
	}

}
