package plugins.world_builder;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.BuildObjects;
import io.gamemachine.messages.GameMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorldBuilderHandler extends GameMessageActor {

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

	public static String name = WorldBuilderHandler.class.getSimpleName();
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
			if (buildObjects.hasAction()) {
				if (buildObjects.action == 1) {
					buildObjects.currentUpdate = getUpdateCount();
					setReply(gameMessage);
				} else if (buildObjects.action == 2) {
					if (buildObjects.hasRequestedUpdateId()) {
						if (updateJournal.containsKey(buildObjects.requestedUpdateId)) {
							Update update = getUpdate(buildObjects.requestedUpdateId);
							BuildObject buildObject = update.buildObject.clone();
							buildObject.updateId = buildObjects.requestedUpdateId;
							gameMessage.buildObjects.addBuildObject(buildObject);
						} else {
							logger.warning("requestedUpdateId not found: " + buildObjects.requestedUpdateId);
						}
					} else {
						logger.warning("action 2 without requested update id");
					}
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
				}
			}
		}
	}

	public static byte[] getBuildObjects() {
		BuildObjects buildObjects = new BuildObjects();
		for (BuildObject buildObject : objectIndex.values()) {
			buildObjects.getBuildObjectList().add(buildObject);
		}
		return buildObjects.toByteArray();
	}
	
	private Update getUpdate(int key) {
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
		BuildObject.db().deleteWhere("build_object_id = ?", existing.id);
		objectIndex.remove(existing.id);
		addUpdate(existing, 2);
	}

	private void create(BuildObject buildObject) {

		BuildObject existing = find(buildObject.id);
		if (existing != null) {
			return;
		}

		buildObject.state = 1;
		buildObject.ownerId = characterId;
		buildObject.updatedAt = System.currentTimeMillis();
		BuildObject.db().save(buildObject);
		objectIndex.put(buildObject.id, buildObject);
		addUpdate(buildObject, 1);
	}

	private void load() {
		for (BuildObject buildObject : BuildObject.db().findAll()) {
			objectIndex.put(buildObject.id, buildObject);

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
