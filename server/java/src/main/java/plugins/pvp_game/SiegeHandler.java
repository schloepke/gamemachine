package plugins.pvp_game;

import com.google.common.base.Strings;

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
				doHit(gameMessage.siegeCommand.hitId, gameMessage.siegeCommand.skillId);
			} else {
				if (gameMessage.siegeCommand.hasStartUse()) {
					setUser(gameMessage.siegeCommand.id, true);
				} else if (gameMessage.siegeCommand.hasEndUse()) {
					setUser(gameMessage.siegeCommand.id, false);
				}
				Grid grid = GameGrid.getGameGrid("mygame", "default", playerId);
				for (TrackData trackData : grid.getAll()) {
					PlayerCommands.sendGameMessage(gameMessage, trackData.id);
				}
			}

		}

	}

	private void setUser(String id, boolean inUse) {
		WorldObject worldObject = ConsumableItemHandler.find(id);
		if (worldObject == null) {
			logger.warning("Siege find failed " + id + " " + playerId);
			return;
		}

		if (inUse) {
			if (Strings.isNullOrEmpty(worldObject.currentUser)) {
				worldObject.currentUser = playerId;
				logger.warning("Siege use " + playerId);
			} else {
				logger.warning("Siege use failed " + playerId);
				return;
			}

		} else {
			if (!Strings.isNullOrEmpty(worldObject.currentUser) && worldObject.currentUser.equals(playerId)) {
				worldObject.currentUser = null;
				logger.warning("Siege release " + playerId);
			} else {
				logger.warning("Siege release failed " + playerId);
				return;
			}

		}
		WorldObject.db().save(worldObject);
		ConsumableItemHandler.wobjects.put(worldObject.id, worldObject);
	}

	private void doHit(String id, String skillId) {
		WorldObject worldObject = ConsumableItemHandler.find(id);
		logger.warning("Siege dohit " + id + " " + skillId);
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

}
