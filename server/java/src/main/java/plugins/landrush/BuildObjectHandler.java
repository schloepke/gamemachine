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
import plugins.combat.Common;
import plugins.combat.VitalsHandler;

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
			if (buildObjects.action > 0) {
				if (buildObjects.action == 1) {
					buildObjects.currentUpdate = getUpdateCount();
					setReply(gameMessage);
				}

				setReply(gameMessage);
			} else {
				BuildObject bo = gameMessage.buildObjects.buildObject.get(0);
				if (bo.action == 1) {
					create(bo);
					setReply(gameMessage);
				} else if (bo.action == 2) {
					remove(bo);
					setReply(gameMessage);
				} else if (bo.action == 5) {
					removeAll();
					setReply(gameMessage);
				} else if (bo.action == 10) {
					updateDoor(bo);
					setReply(gameMessage);
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
			for (int i = start; i<= end;i++) {
				Update update = getUpdate(i);
				if (update == null) {
					System.out.println("Update not found with id "+i);
					continue;
				}
				BuildObject buildObject = update.buildObject.clone();
				buildObject.updateId = i;
				buildObjects.addBuildObject(buildObject);
			}
		}
		
		return buildObjects.toByteArray();
	}
	
	private void broadcast(GameMessage gameMessage) {
		for (Grid grid : GameGrid.gridsStartingWith("default")) {
			for (TrackData trackData : grid.getAll()) {
				if (trackData.entityType != TrackData.EntityType.Player) {
					continue;
				}
				PlayerCommands.sendGameMessage(gameMessage.clone(), trackData.id);
			}
		}
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
		gameMessage.buildObjects.action = 10;
		gameMessage.buildObjects.getBuildObjectList().add(existing);
		broadcast(gameMessage);
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

	private BuildObject find(String id) {
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

		existing.state = 2;
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
		buildObject.state = 1;
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
		logger.warning(objectIndex.size()+" build objects loaded");
	}

	private void removeGridAndVitals(BuildObject buildObject) {
		if (buildObject.isDestructable) {
			Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "build_objects", buildObject.zone);
			grid.remove(buildObject.id);
			VitalsHandler handler = VitalsHandler.getHandler("build_objects", buildObject.zone);
			handler.remove(buildObject.id);
		}
	}
	
	private void setGridAndVitals(BuildObject buildObject) {
		if (buildObject.isDestructable) {
			Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "build_objects", buildObject.zone);
			grid.set(buildObject.id, buildObject.x, buildObject.y, buildObject.z, TrackData.EntityType.Object);
			VitalsHandler handler = VitalsHandler.getHandler("build_objects", buildObject.zone);
			handler.findOrCreateObjectVitals(buildObject.id, Vitals.VitalsType.Object.number, buildObject.zone);
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
