package plugins.landrush;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.BuildObjects;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.VitalsHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BuildObjectHandler extends GameMessageActor {

	public class Update {
		BuildObject buildObject;
		Long timestamp;
		int action = 0;

		public Update(BuildObject buildObject, int action) {
			this.buildObject = buildObject;
			this.action = action;
			this.timestamp = System.currentTimeMillis();
		}
	}

	public static String name = BuildObjectHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static ConcurrentHashMap<String, BuildObject> objectIndex = new ConcurrentHashMap<String, BuildObject>();
	public static ConcurrentHashMap<Integer, Update> updateJournal = new ConcurrentHashMap<Integer, Update>();
	public static AtomicInteger updateCount = new AtomicInteger();

	@Override
	public void awake() {
		load();

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		BuildObjects buildObjects = gameMessage.buildObjects;

		if (exactlyOnce(gameMessage)) {
			if (buildObjects.action != BuildObjects.Action.None) {
				if (buildObjects.action == BuildObjects.Action.GetStatus) {
					buildObjects.currentUpdate = getUpdateCount();
					setReply(gameMessage);
				}

				setReply(gameMessage);
			} else {
				BuildObject bo = gameMessage.buildObjects.buildObject.get(0);
				if (bo.action == BuildObject.Action.Create) {
					create(bo);
					setReply(gameMessage);
				} else if (bo.action == BuildObject.Action.Remove) {
					remove(bo);
					setReply(gameMessage);
				} else if (bo.action == BuildObject.Action.RemoveAll) {
					removeAll();
					setReply(gameMessage);
				} else if (bo.action == BuildObject.Action.UpdateProp) {
					updateDoor(bo);
					setReply(gameMessage);
				}
			}
		} else {
			if (gameMessage.buildObjects.buildObject != null && gameMessage.buildObjects.buildObject.size() >= 1) {
				BuildObject bo = gameMessage.buildObjects.buildObject.get(0);
				if (bo != null && bo.action == BuildObject.Action.SetHealth) {
					setHealth(bo);
				}
			}

		}
	}

	public static byte[] getBuildObjects(int start, int end) {
		BuildObjects buildObjects = new BuildObjects();
		if (start == 0 && end == 0) {
			for (BuildObject buildObject : objectIndex.values()) {
				buildObjects.getBuildObjectList().add(buildObject);
			}
		} else {
			for (int i = start; i <= end; i++) {
				Update update = getUpdate(i);
				if (update == null) {
					System.out.println("Update not found with id " + i);
					continue;
				}
				BuildObject buildObject = update.buildObject.clone();
				buildObject.updateId = i;
				buildObjects.addBuildObject(buildObject);
			}
		}

		return buildObjects.toByteArray();
	}

	public static void setHealth(String id, int health) {
		BuildObjects buildObjects = new BuildObjects();
		BuildObject buildObject = new BuildObject();
		buildObject.id = id;
		buildObject.health = health;
		buildObject.action = BuildObject.Action.SetHealth;
		buildObjects.addBuildObject(buildObject);
		GameMessage gameMessage = new GameMessage();
		gameMessage.buildObjects = buildObjects;
		BuildObjectHandler.tell(gameMessage, BuildObjectHandler.name);
	}

	private void setHealth(BuildObject buildObject) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return;
		}
		// logger.warning("setHealth "+buildObject.health);
		if (buildObject.health <= 0) {
			remove(buildObject);
		} else {
			existing.health = buildObject.health;
			BuildObject.db().save(existing);
		}
	}

	private void broadcast(GameMessage gameMessage, int zone) {
		Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default", zone);
		for (TrackData trackData : grid.getAll()) {
			if (trackData.entityType != TrackData.EntityType.Player) {
				continue;
			}
			PlayerCommands.sendGameMessage(gameMessage.clone(), trackData.id);
		}
	}

	public static BuildObject updateRotation(BuildObject buildObject) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return null;
		}

		buildObject.ownerId = existing.ownerId;
		existing.rx = buildObject.rx;
		existing.ry = buildObject.ry;
		existing.rz = buildObject.rz;
		existing.rw = buildObject.rw;
		existing.updatedAt = System.currentTimeMillis();
		BuildObject.db().save(existing);
		return existing;
	}
	
	private void updateDoor(BuildObject buildObject) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return;
		}

		existing.doorStatus = buildObject.doorStatus;
		existing.updatedAt = System.currentTimeMillis();
		BuildObject.db().save(existing);
		addUpdate(existing, 10);

		GameMessage gameMessage = new GameMessage();
		gameMessage.buildObjects = new BuildObjects();
		gameMessage.buildObjects.action = BuildObjects.Action.PropUpdate;
		gameMessage.buildObjects.getBuildObjectList().add(existing);
		broadcast(gameMessage,buildObject.zone);
	}

	private static Update getUpdate(int key) {
		if (updateJournal.containsKey(key)) {
			return updateJournal.get(key);
		} else {
			return null;
		}
	}

	private Update addUpdate(BuildObject buildObject, int action) {
		int count = updateCount.incrementAndGet();
		Update update = new Update(buildObject.clone(), action);
		updateJournal.put(count, update);
		return update;
	}

	private int getUpdateCount() {
		return updateCount.get();
	}

	public static BuildObject find(String id) {
		if (!objectIndex.containsKey(id)) {
			BuildObject buildObject = BuildObject.db().findFirst("build_object_id = ?", id);
			if (buildObject == null) {
				return null;
			} else {
				objectIndex.put(id, buildObject);
			}
		}
		return objectIndex.get(id);
	}

	private void removeAll() {
		for (BuildObject bo : objectIndex.values()) {
			if (bo.ownerId.equals(characterId)) {
				remove(bo);
			}
		}
	}

	private void remove(BuildObject buildObject) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return;
		}

		existing.state = BuildObject.State.Removed;
		existing.updatedAt = System.currentTimeMillis();
		removeGridAndVitals(existing);
		BuildObject.db().deleteWhere("build_object_id = ?", existing.id);
		objectIndex.remove(existing.id);
		addUpdate(existing, 2);
	}

	private void create(BuildObject buildObject) {

		BuildObject existing = find(buildObject.id);
		if (existing != null) {
			return;
		}

		buildObject.zone = CharacterService.instance().getZone(buildObject.ownerId);
		buildObject.state = BuildObject.State.Active;
		buildObject.ownerId = characterId;
		buildObject.updatedAt = System.currentTimeMillis();
		BuildObject.db().save(buildObject);
		objectIndex.put(buildObject.id, buildObject);
		addUpdate(buildObject, 1);
		setGridAndVitals(buildObject);
	}

	private void load() {
		for (BuildObject buildObject : BuildObject.db().findAll()) {
			objectIndex.put(buildObject.id, buildObject);
			setGridAndVitals(buildObject);
		}
		logger.warning(objectIndex.size() + " build objects loaded");
	}

	private void removeGridAndVitals(BuildObject buildObject) {
		if (buildObject.isDestructable) {
			Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "build_objects", buildObject.zone);
			grid.remove(buildObject.id);
			VitalsHandler.remove(buildObject.id);
		}
	}

	private void setGridAndVitals(BuildObject buildObject) {
		if (buildObject.isDestructable) {
			Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "build_objects", buildObject.zone);
			grid.set(buildObject.id, buildObject.x, buildObject.y, buildObject.z, TrackData.EntityType.BuildObject);
			VitalsHandler.get(buildObject.id, Vitals.VitalsType.BuildObject, buildObject.zone);
		}
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		String characterId = PlayerService.getInstance().getCharacter(playerId);

	}

}
