package pvp_game;

import io.gamemachine.core.Commands;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.WorldObject;
import io.gamemachine.messages.WorldObjects;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ConsumableItemHandler extends GameMessageActor {

	public static String name = ConsumableItemHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static ConcurrentHashMap<String, WorldObject> wobjects = new ConcurrentHashMap<String, WorldObject>();
	public static AtomicLong counter = new AtomicLong();
	private Grid worldObjectGrid;
	private Grid defaultGrid;

	@Override
	public void awake() {
		worldObjectGrid = GameGrid.getGameGrid(Common.gameId, "world_objects");
		defaultGrid = GameGrid.getGameGrid(Common.gameId, "default");
		load();
		scheduleOnce(5000l, new GameMessage());

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasWorldObjects()) {

			for (WorldObject worldObject : gameMessage.worldObjects.worldObject) {
				if (worldObject.action == 1) {
					deploy(worldObject);
				} else if (worldObject.action == 2) {
					updatePosition(worldObject);
				} else if (worldObject.action == 3) {
					create(worldObject);
				} else if (worldObject.action == 5) {
					remove(worldObject.id, worldObject.ownerId);
				}
			}
			sendWorldObjects();
		} else {
			sendWorldObjects();
			scheduleOnce(5000l, new GameMessage());
		}

	}

	private void sendWorldObjects() {
		for (TrackData trackData : defaultGrid.getAll()) {
			GameMessage gameMessage = new GameMessage();
			WorldObjects worldObjects = new WorldObjects();
			List<String> targets = Common.getTargetsInRange(-1, trackData.x, trackData.y, trackData.z, "world_objects");
			for (String target : targets) {
				if (wobjects.containsKey(target)) {
					WorldObject worldObject = wobjects.get(target);
					worldObjects.addWorldObject(worldObject);
				}
			}
			gameMessage.worldObjects = worldObjects;
			PlayerCommands.sendGameMessage(gameMessage, trackData.id);
		}
	}

	private String genId(WorldObject worldObject) {
		return worldObject.ownerId + System.currentTimeMillis() + counter.getAndIncrement();
	}

	private void remove(String id, String ownerId) {
		WorldObject.db().deleteWhere("world_object_id = ?", id);
	}

	private WorldObject find(String id) {
		return WorldObject.db().findFirst("world_object_id = ?", id);
	}

	private void deploy(WorldObject worldObject) {
		PlayerItem playerItem = PlayerItem.db().findFirst("player_item_id = ? and player_item_player_id = ?",
				worldObject.playerItemId, worldObject.ownerId);
		if (playerItem == null) {
			logger.warning("Player does not own " + worldObject.playerItemId);
			return;
		}
		Commands.removePlayerItem(playerId, worldObject.playerItemId, 1);
		create(worldObject);
	}

	private void create(WorldObject worldObject) {
		if (worldObject.hasId()) {
			WorldObject existing = find(worldObject.id);
			if (existing != null) {
				return;
			}
		} else {
			worldObject.id = genId(worldObject);
		}

		WorldObject.db().save(worldObject);
		wobjects.put(worldObject.id, worldObject);
		worldObjectGrid.set(toTrackData(worldObject));
	}

	private void updatePosition(WorldObject worldObject) {
		WorldObject existing = find(worldObject.id);
		existing.x = worldObject.x;
		existing.y = worldObject.y;
		existing.z = worldObject.z;
		existing.rx = worldObject.rx;
		existing.ry = worldObject.ry;
		existing.rz = worldObject.rz;
		existing.rw = worldObject.rw;

		WorldObject.db().save(existing);
		wobjects.put(existing.id, existing);
		worldObjectGrid.set(toTrackData(existing));
	}

	private TrackData toTrackData(WorldObject worldObject) {
		TrackData trackData = new TrackData();
		trackData.x = worldObject.x;
		trackData.y = worldObject.y;
		trackData.z = worldObject.z;
		trackData.id = worldObject.id;
		return trackData;
	}

	private void load() {
		for (WorldObject worldObject : WorldObject.db().findAll()) {
			wobjects.put(worldObject.id, worldObject);
			worldObjectGrid.set(toTrackData(worldObject));
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub

	}

}
