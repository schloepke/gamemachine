package plugins.landrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.BuildObjectDatas;
import io.gamemachine.messages.BuildObjects;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.net.Connection;
import io.gamemachine.objectdb.Cache;
import plugins.core.combat.VitalsHandler;

// Player initial load get everything
// subsequent loads only other player updates
public class BuildObjectHandler extends GameMessageActor {

	public static class Update {
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
	private static final Logger logger = LoggerFactory.getLogger(BuildObjectHandler.class);

	public static Map<String, BuildObject> objectIndex = new ConcurrentHashMap<String, BuildObject>();
	public static AtomicInteger updateCount = new AtomicInteger();
	public static Cache<String, Integer> versionCache = new Cache<String, Integer>(60, 10000);
	public static Map<String, Map<String, Integer>> playerVersions = new ConcurrentHashMap<String, Map<String, Integer>>();

	@Override
	public void awake() {
		load();

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		BuildObjects buildObjects = gameMessage.buildObjects;
		if (exactlyOnce(gameMessage)) {
			BuildObject bo = gameMessage.buildObjects.buildObject.get(0);
			processBuildObject(bo, characterId);
			setReply(gameMessage);
		} else {
			if (buildObjects.action == BuildObjects.Action.GetStatus) {
				buildObjects.currentUpdate = updateCount.get();
				PlayerMessage.tell(gameMessage, playerId);
			} else if (buildObjects.buildObject != null && buildObjects.buildObject.size() >= 1) {

				if (buildObjects.action == BuildObjects.Action.HttpSave) {
					for (BuildObject bo : buildObjects.buildObject) {
						processBuildObject(bo, characterId);
					}

				} else {
					BuildObject bo = buildObjects.buildObject.get(0);
					if (bo != null && bo.action == BuildObject.Action.SetHealth) {
						setHealth(bo, characterId);
					}
				}
			}

		}
	}

	private void processBuildObject(BuildObject bo, String characterId) {
		if (bo.action == BuildObject.Action.UpdateProp) {
			updateDoor(bo, characterId);
		} else if (bo.action == BuildObject.Action.Save) {
			save(bo, characterId, characterId);
		} else {
			logger.warn("Invalid action " + bo.action);
		}
	}

	private static Integer getPlayerVersion(String characterId, String id) {
		if (!playerVersions.containsKey(characterId)) {
			logger.warn("No version info for " + characterId);
			return null;
		}
		Map<String, Integer> versions = playerVersions.get(characterId);
		if (versions.containsKey(id)) {
			return versions.get(id);
		} else {
			return null;
		}
	}

	private static void setPlayerVersion(String characterId, String id, int version) {
		if (!playerVersions.containsKey(characterId)) {
			playerVersions.put(characterId, new ConcurrentHashMap<String, Integer>());
		}
		Map<String, Integer> versions = playerVersions.get(characterId);
		versions.put(id, version);
	}

	public static byte[] getBuildObjects(String playerId, boolean all) {
		BuildObjects buildObjects = new BuildObjects();
		String characterId = PlayerService.getInstance().getCharacterId(playerId);

		if (all) {
			playerVersions.put(characterId, new ConcurrentHashMap<String, Integer>());
			for (BuildObject bo : objectIndex.values()) {
				buildObjects.addBuildObject(bo);
				setPlayerVersion(characterId, bo.id, bo.version);
			}
		} else {
			Map<String, Integer> versions = versionCache.asMap();

			for (String key : versions.keySet()) {
				Integer version = versions.get(key);
				Integer playerVersion = getPlayerVersion(characterId, key);

				// Up to date
				if (playerVersion == version) {
					continue;
				}

				// logger.warn(key +" = "+version + " "+ playerVersion);

				BuildObject buildObject = objectIndex.get(key);

				// Only need to send changes not initiated by player
				if (buildObject.ownerId.equals(characterId)) {

					if (buildObject.isTerrainEdit || buildObject.isGroundBlock) {
						continue;

					} else {
						if (buildObject.state != BuildObject.State.Removed) {
							continue;
						}
					}

				}

				buildObjects.addBuildObject(buildObject);
				setPlayerVersion(characterId, key, version);
			}
		}

		return buildObjects.toByteArray();
	}

	private static void broadcast(GameMessage gameMessage, String zone) {
		Grid grid = GridService.getInstance().getGrid(zone, "default");
		for (TrackData trackData : grid.getAll()) {
			if (trackData.entityType != TrackData.EntityType.Player) {
				continue;
			}
			PlayerMessage.tell(gameMessage.clone(), trackData.id);
		}
	}

	public static BuildObject updateRotation(BuildObject buildObject, String playerId) {
		String characterId = PlayerService.getInstance().getCharacterId(playerId);
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return null;
		}

		existing.rx = buildObject.rx;
		existing.ry = buildObject.ry;
		existing.rz = buildObject.rz;
		existing.rw = buildObject.rw;
		//save(existing, characterId, characterId);
		return existing;
	}

	private void updateDoor(BuildObject buildObject, String characterId) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return;
		}

		existing.doorStatus = buildObject.doorStatus;
		save(existing, characterId, characterId);

		GameMessage gameMessage = new GameMessage();
		gameMessage.buildObjects = new BuildObjects();
		gameMessage.buildObjects.action = BuildObjects.Action.PropUpdate;
		gameMessage.buildObjects.getBuildObjectList().add(existing);
		broadcast(gameMessage, existing.zone);
	}

	public static BuildObject find(String id) {
		if (!objectIndex.containsKey(id)) {
			BuildObjectDatas data = BuildObjectDatas.db().findFirst("build_object_datas_id = ?", id);
			if (data == null) {
				return null;
			} else {
				byte[] bytes = Base64.decodeBase64(data.data);
				BuildObject buildObject = BuildObject.parseFrom(bytes);
				objectIndex.put(id, buildObject);
			}
		}
		return objectIndex.get(id);
	}

	public static void save(BuildObject buildObject, String characterId, String origin) {
		Zone zone = CharacterService.instance().getZone(characterId);

		BuildObject existing = null;
		BuildObjectDatas data = BuildObjectDatas.db().findFirst("build_object_datas_id = ?", buildObject.id);
		if (data == null) {
			data = new BuildObjectDatas();
			data.id = buildObject.id;
			data.characterId = characterId;
			buildObject.version += 1;
		} else {
			byte[] bytes = Base64.decodeBase64(data.data);
			existing = BuildObject.parseFrom(bytes);
		}

		buildObject.ownerId = characterId;
		buildObject.zone = zone.name;
		buildObject.updatedAt = System.currentTimeMillis();

		if (existing != null) {
			buildObject.version = existing.version + 1;
		} else {
			buildObject.version += 1;
		}

		data.data = Base64.encodeBase64String(buildObject.toByteArray());
		io.gamemachine.orm.models.BuildObjectDatas.openTransaction();
		BuildObjectDatas.db().save(data, true);
		io.gamemachine.orm.models.BuildObjectDatas.commitTransaction();

		versionCache.set(buildObject.id, buildObject.version);
		if (origin.equals(characterId)) {
			setPlayerVersion(characterId, buildObject.id, buildObject.version);
		}
		

		objectIndex.put(buildObject.id, buildObject);
		updateCount.getAndIncrement();

		if (buildObject.state == BuildObject.State.Removed) {
			removeGridAndVitals(buildObject);
		} else {
			setGridAndVitals(buildObject);
		}
	}

	public static boolean exists(String id) {
		return find(id) != null;
	}

	private void load() {
		for (BuildObjectDatas data : BuildObjectDatas.db().findAll()) {
			byte[] bytes = Base64.decodeBase64(data.data);
			BuildObject buildObject = BuildObject.parseFrom(bytes);
			if (buildObject.state == BuildObject.State.Removed) {
				continue;
			}
			objectIndex.put(buildObject.id, buildObject);
			setGridAndVitals(buildObject);
		}
		logger.warn(objectIndex.size() + " build objects loaded");
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

	private void setHealth(BuildObject buildObject, String characterId) {
		BuildObject existing = find(buildObject.id);
		if (existing == null) {
			return;
		}
		logger.warn("setHealth " + buildObject.health);
		if (buildObject.health <= 0) {
			existing.state = BuildObject.State.Removed;
		}
		existing.health = buildObject.health;
		save(existing, existing.ownerId, "system");
	}

	private static void setGridAndVitals(BuildObject buildObject) {
		if (!buildObject.isTerrainEdit && !buildObject.isGroundBlock) {
			if (buildObject.isDestructable) {
				Grid grid = GridService.getInstance().getGrid(buildObject.zone, "build_objects");
				grid.set(buildObject.id, buildObject.x, buildObject.y, buildObject.z, TrackData.EntityType.BuildObject);
				VitalsHandler.get(buildObject.id, Vitals.VitalsType.BuildObject, buildObject.zone);
			}
		}

	}

	private static void removeGridAndVitals(BuildObject buildObject) {
		if (!buildObject.isTerrainEdit && !buildObject.isGroundBlock) {
			if (buildObject.isDestructable) {
				logger.warn("BuildObject "+buildObject.id+"  removed from grid");
				Grid grid = GridService.getInstance().getGrid(buildObject.zone, "build_objects");
				grid.remove(buildObject.id);
				VitalsHandler.remove(buildObject.id);
			}
		}
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerDisconnect(String playerId) {

	}

}
