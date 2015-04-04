package pvp_game;

import io.gamemachine.core.Commands;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.PlayerVitalsHandler;
import io.gamemachine.messages.Bounds;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.WorldObject;
import io.gamemachine.messages.WorldObjects;
import io.gamemachine.pathfinding.grid.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.google.common.base.Strings;

public class ConsumableItemHandler extends GameMessageActor {

	public static String name = ConsumableItemHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private ConcurrentHashMap<String, Long> toRemove = new ConcurrentHashMap<String, Long>();
	public static ConcurrentHashMap<String, WorldObject> playerAttached = new ConcurrentHashMap<String, WorldObject>();
	public static ConcurrentHashMap<String, WorldObject> wobjects = new ConcurrentHashMap<String, WorldObject>();
	public static AtomicLong counter = new AtomicLong();
	private Grid worldObjectGrid;
	private Grid defaultGrid;

	@Override
	public void awake() {
		Commands.clientManagerRegister(name);
		load();
		scheduleOnce(5000l, new GameMessage());

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (playerId != null) {
			worldObjectGrid = GameGrid.getGameGrid(Common.gameId, "world_objects",playerId);
			defaultGrid = GameGrid.getGameGrid(Common.gameId, "default",playerId);
		}
				
		if (gameMessage.hasUseItem()) {
			
			// Make PlayerItemManager into game message actor and move this there
			//logger.warning(playerId+ " useItem "+gameMessage.useItem.id+" action "+gameMessage.useItem.action);
			PlayerItemManager.handleUseItem(gameMessage.useItem,playerId);
			for (TrackData trackData : defaultGrid.getAll()) {
				PlayerCommands.sendGameMessage(gameMessage, trackData.id);
			}
			return;
		}
		
		if (gameMessage.hasEquippedItem()) {
			String itemId = PlayerItemManager.getEquippedItem(gameMessage.equippedItem.playerId);
			if (itemId != null) {
				logger.warning(playerId+" requested equipped for "+ gameMessage.equippedItem.playerId+ " equipped with "+itemId);
				gameMessage.equippedItem.id = itemId;
				PlayerCommands.sendGameMessage(gameMessage, playerId);
			} else {
				logger.warning(playerId+" requested equipped for "+ gameMessage.equippedItem.playerId+" failed");
			}
			
			return;
		}

		if (gameMessage.hasWorldObjects()) {

			for (WorldObject worldObject : gameMessage.worldObjects.worldObject) {
				if (worldObject.action == 1) {
					if (gameMessage.hasBounds()) {
						deployInBounds(worldObject, gameMessage.bounds);
					} else {
						deploy(worldObject);
					}

				} else if (worldObject.action == 2) {
					updatePosition(worldObject);
				} else if (worldObject.action == 3) {
					create(worldObject);
				} else if (worldObject.action == 5) {
					remove(worldObject.id, worldObject.ownerId);
				} else if (worldObject.action == 6) {
					updateState(worldObject);
				}
			}
			sendWorldObjects();
		} else {
			sendWorldObjects();
			removeDead();
			scheduleOnce(5000l, new GameMessage());
		}

	}

	private void removeDead() {
		List<String> idsToRemove = new ArrayList<String>();
		for (WorldObject worldObject : wobjects.values()) {
			if (worldObject.health <= 0 && worldObject.playerItemId != null) {
				if (toRemove.containsKey(worldObject.id)) {
					Long diedAt = toRemove.get(worldObject.id);
					if (System.currentTimeMillis() - diedAt > 30l) {
						idsToRemove.add(worldObject.id);
						logger.warning("Added " + worldObject.id+" to dead queue");
					}
				} else {
					toRemove.put(worldObject.id, System.currentTimeMillis());
				}
			}
		}
		
		
		for (String id : idsToRemove) {
			WorldObject.db().deleteWhere("world_object_id = ?", id);
			wobjects.remove(id);
			toRemove.remove(id);
			for (Grid grid : GameGrid.gridsStartingWith("default")) {
				String objectGridName = grid.name.replaceAll("default", "world_objects");
				Grid objectGrid = GameGrid.xgetGameGrid(objectGridName);
				objectGrid.remove(id);
			}
			logger.warning("Removed " + id+" from worldObjects");
		}
		
	}
	

	private void sendWorldObjects() {
		
		for (Grid grid : GameGrid.gridsStartingWith("default")) {
			String objectGridName = grid.name.replaceAll("default", "world_objects");
			Grid objectGrid = GameGrid.xgetGameGrid(objectGridName);
			//logger.warning("grid "+grid.name+" objectGrid "+objectGrid.name);
			
			for (TrackData trackData : grid.getAll()) {
				GameMessage gameMessage = new GameMessage();
				WorldObjects worldObjects = new WorldObjects();
				List<String> targets = Common
						.getTargetsInRange(150, trackData.x, trackData.y, trackData.z, objectGrid);
				for (String target : targets) {
					if (wobjects.containsKey(target)) {
						WorldObject worldObject = wobjects.get(target);
						if (worldObject.hasHealth() && worldObject.health <= 0) {
							if (worldObject.hasPlayerItemId()) {
								//remove(worldObject.id);
								//continue;
							}
						}
						worldObjects.addWorldObject(worldObject);
					}
				}
				gameMessage.worldObjects = worldObjects;
				PlayerCommands.sendGameMessage(gameMessage, trackData.id);
			}
		}
		
	}
	
	private String genId(WorldObject worldObject) {
		return worldObject.ownerId + System.currentTimeMillis() + counter.getAndIncrement();
	}

	public static void remove(String id, String playerId) {
		WorldObject.db().deleteWhere("world_object_id = ?", id);
		wobjects.remove(id);
		GameGrid.getGameGrid(Common.gameId, "world_objects",playerId).remove(id);
	}

	public static WorldObject find(String id) {
		if (!wobjects.containsKey(id)) {
			WorldObject worldObject = WorldObject.db().findFirst("world_object_id = ?", id);
			if (worldObject == null) {
				//logger.warning("Cannot find worldobject with id " + id);
				return null;
			} else {
				wobjects.put(id, worldObject);
			}
		} 
		return wobjects.get(id);
	}

	private void deployInBounds(WorldObject worldObject, Bounds bounds) {
		if (!canSpawn(bounds)) {
			logger.warning("Objects in way of deploying " + worldObject.playerItemId);
			return;
		}

		PlayerItem playerItem = PlayerItemManager.playerItem(worldObject.playerItemId, worldObject.ownerId);
		if (playerItem == null) {
			logger.warning("Player does not own " + worldObject.playerItemId);
			return;
		}

		worldObject.id = worldObject.playerItemId + "_" + worldObject.ownerId;
		if (worldObject.playerItemId.equals("ship")) {
			if (playerAttached.containsKey(worldObject.id)) {
				logger.warning("Player already has instance of " + worldObject.playerItemId);
				return;
			}
			playerAttached.put(worldObject.id, worldObject);
		}
		GameMessage gameMessage = new GameMessage();
		WorldObjects worldObjects = new WorldObjects();
		worldObjects.addWorldObject(worldObject);
		gameMessage.worldObjects = worldObjects;
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	private Boolean canSpawn(Bounds bounds) {
		io.gamemachine.util.Vector3 min = new io.gamemachine.util.Vector3(bounds.min.x, bounds.min.y, 0l);
		io.gamemachine.util.Vector3 max = new io.gamemachine.util.Vector3(bounds.max.x, bounds.max.y, 0l);
		BoundingBox box = new BoundingBox(min, max);
		io.gamemachine.util.Vector3 entityPosition;
		for (TrackData trackData : defaultGrid.getAll()) {
			entityPosition = Common.toVector3(trackData.x, trackData.y);
			if (box.contains(entityPosition)) {
				return false;
			}
		}
		return true;
	}

	private void deploy(WorldObject worldObject) {
		PlayerItem playerItem = PlayerItemManager.playerItem(worldObject.playerItemId, worldObject.ownerId);
		if (playerItem == null) {
			logger.warning("Player does not own " + worldObject.playerItemId);
			return;
		}
		PlayerItemManager.removePlayerItem(playerId, worldObject.playerItemId, 1);
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

		if (worldObject.hasPlayerItemId()) {
			PlayerItem global = PlayerItemManager.playerItemGlobal(worldObject.playerItemId);
			if (global != null) {
				worldObject.maxHealth = global.maxHealth;
				worldObject.health = global.maxHealth;
			}
		}
		
		Grid grid;
		if (Strings.isNullOrEmpty(worldObject.grid)) {
			worldObject.grid = worldObjectGrid.name;
			grid = worldObjectGrid;
		} else {
			grid = GameGrid.xgetGameGrid(worldObject.grid);
		}
		
		WorldObject.db().save(worldObject);
		wobjects.put(worldObject.id, worldObject);
		grid.set(toTrackData(worldObject));
	}

	private void updatePosition(WorldObject worldObject) {
		WorldObject existing = find(worldObject.id);
		if (existing == null) {
			logger.warning("Worldobject with id "+worldObject.id+" not found");
		}
		
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
	
	private void updateState(WorldObject worldObject) {
		WorldObject existing = find(worldObject.id);
		if (existing == null) {
			logger.warning("Worldobject with id "+worldObject.id+" not found");
		}
		
		if (!existing.ownerId.equals(characterId) && !GuildHandler.inGuild(existing.ownerId,playerId)) {
			logger.warning("Door "+worldObject.id+" access denied for "+characterId+" "+playerId);
			return;
		}
		
		existing.state = worldObject.state;

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
		trackData.entityType = TrackData.EntityType.OTHER;
		return trackData;
	}

	private void load() {
		for (WorldObject worldObject : WorldObject.db().findAll()) {
			wobjects.put(worldObject.id, worldObject);
			worldObjectGrid = GameGrid.xgetGameGrid(worldObject.grid);
			worldObjectGrid.set(toTrackData(worldObject));
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		logger.warning("Player disconnect " + playerId);
		PlayerVitalsHandler.remove(playerId);
		List<String> removed = new ArrayList<String>();
		String characterId = PlayerService.getInstance().getCharacter(playerId);
		for (WorldObject worldObject : playerAttached.values()) {
			if (worldObject.ownerId.equals(characterId)) {
				logger.warning("Removing  " + worldObject.id + " from grid");
				defaultGrid.remove(worldObject.id);
				removed.add(worldObject.id);
			}
		}
		for (String id : removed) {
			playerAttached.remove(id);
		}
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub

	}

}
