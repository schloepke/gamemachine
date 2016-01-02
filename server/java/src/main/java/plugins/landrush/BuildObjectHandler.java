package plugins.landrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

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
import io.gamemachine.regions.ZoneService;
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

	private BuildObjectCache cache;
	private Zone zone;
	
	

	private static Integer getPlayerVersion(String characterId, String id, BuildObjectCache cache) {
		if (!cache.playerVersions.containsKey(characterId)) {
			logger.warn("No version info for " + characterId);
			return null;
		}
		Map<String, Integer> versions = cache.playerVersions.get(characterId);
		if (versions.containsKey(id)) {
			return versions.get(id);
		} else {
			return null;
		}
	}

	private static void setPlayerVersion(String characterId, String id, int version, BuildObjectCache cache) {
		if (!cache.playerVersions.containsKey(characterId)) {
			cache.playerVersions.put(characterId, new ConcurrentHashMap<String, Integer>());
		}
		Map<String, Integer> versions = cache.playerVersions.get(characterId);
		versions.put(id, version);
	}

	public static byte[] getBuildObjects(String playerId, boolean all) {
		Zone zone =  PlayerService.getInstance().getZone(playerId);
		BuildObjectCache cache = BuildObjectCache.get(zone.name);
		
		BuildObjects buildObjects = new BuildObjects();
		String characterId = PlayerService.getInstance().getCharacterId(playerId);

		if (all) {
			cache.playerVersions.put(characterId, new ConcurrentHashMap<String, Integer>());
			for (BuildObject bo : cache.objectIndex.values()) {
				buildObjects.addBuildObject(bo);
				setPlayerVersion(characterId, bo.id, bo.version,cache);
			}
		} else {
			Map<String, Integer> versions = cache.versionCache.asMap();

			for (String key : versions.keySet()) {
				Integer version = versions.get(key);
				Integer playerVersion = getPlayerVersion(characterId, key,cache);

				// Up to date
				if (playerVersion == version) {
					continue;
				}

				// logger.warn(key +" = "+version + " "+ playerVersion);

				BuildObject buildObject = cache.objectIndex.get(key);

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
				setPlayerVersion(characterId, key, version,cache);
			}
		}

		return buildObjects.toByteArray();
	}

	public static boolean exists(String id, String zone) {
		return find(id, zone) != null;
	}
	
	public static BuildObject find(String id, String zone) {
		BuildObjectCache cache = BuildObjectCache.get(zone);
		
		if (!cache.objectIndex.containsKey(id)) {
			BuildObjectDatas data = BuildObjectDatas.db().findFirst("build_object_datas_id = ? AND build_object_datas_zone = ?", id, zone);
			if (data == null) {
				return null;
			} else {
				byte[] bytes = Base64.decodeBase64(data.dataText);
				BuildObject buildObject = BuildObject.parseFrom(bytes);
				cache.objectIndex.put(id, buildObject);
			}
		}
		return cache.objectIndex.get(id);
	}

	public static void save(BuildObject buildObject, String characterId, String origin) {
		Zone zone =  CharacterService.instance().getZone(characterId);
		BuildObjectCache cache = BuildObjectCache.get(zone.name);
		
		BuildObject existing = null;
		BuildObjectDatas data = BuildObjectDatas.db().findFirst("build_object_datas_id = ? AND build_object_datas_zone = ?", buildObject.id, zone.name);
		if (data == null) {
			data = new BuildObjectDatas();
			data.id = buildObject.id;
			data.characterId = characterId;
			data.zone = zone.name;
			data.group = buildObject.group;
			buildObject.version += 1;
		} else {
			byte[] bytes = Base64.decodeBase64(data.dataText);
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

		data.dataText = Base64.encodeBase64String(buildObject.toByteArray());
		io.gamemachine.orm.models.BuildObjectDatas.openTransaction();
		BuildObjectDatas.db().save(data, true);
		io.gamemachine.orm.models.BuildObjectDatas.commitTransaction();

		cache.versionCache.set(buildObject.id, buildObject.version);
		if (origin.equals(characterId)) {
			setPlayerVersion(characterId, buildObject.id, buildObject.version,cache);
		}
		

		cache.objectIndex.put(buildObject.id, buildObject);
		cache.updateCount.getAndIncrement();

		if (buildObject.state == BuildObject.State.Removed) {
			removeGridAndVitals(buildObject);
		} else {
			setGridAndVitals(buildObject);
		}
	}
	
	public static void setHealth(String id, int health, String zone) {
		BuildObject existing = find(id, zone);
		if (existing == null) {
			return;
		}
		if (health <= 0) {
			existing.state = BuildObject.State.Removed;
		}
		existing.health = health;
		save(existing, existing.ownerId, "system");
	}

	public static void setGridAndVitals(BuildObject buildObject) {
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
		Zone zone =  PlayerService.getInstance().getZone(playerId);
		BuildObject existing = find(buildObject.id, zone.name);
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

	@Override
	public void awake() {
		for (Zone z : ZoneService.staticZones()) {
			zone = z;
			cache = BuildObjectCache.get(zone.name);
			load();
		}
		cache = null;
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		zone =  CharacterService.instance().getZone(characterId);
		cache = BuildObjectCache.get(zone.name);
		
		BuildObjects buildObjects = gameMessage.buildObjects;
		if (exactlyOnce(gameMessage)) {
			BuildObject bo = gameMessage.buildObjects.buildObject.get(0);
			processBuildObject(bo, characterId);
			setReply(gameMessage);
		} else {
			if (buildObjects.action == BuildObjects.Action.GetStatus) {
				buildObjects.currentUpdate = cache.updateCount.get();
				PlayerMessage.tell(gameMessage, playerId);
			} else if (buildObjects.buildObject != null && buildObjects.buildObject.size() >= 1) {

				if (buildObjects.action == BuildObjects.Action.HttpSave) {
					for (BuildObject bo : buildObjects.buildObject) {
						processBuildObject(bo, characterId);
					}

				}
			}
		}
	}

	private void processBuildObject(BuildObject bo, String characterId) {
		if (bo.action == BuildObject.Action.UpdateProp) {
			updateDoor(bo);
		} else if (bo.action == BuildObject.Action.Save) {
			if (!Strings.isNullOrEmpty(bo.ownerId) && bo.ownerId.equals("system")) {
				save(bo, "system", "system");
			} else {
				save(bo, characterId, characterId);
			}
			
		} else {
			logger.warn("Invalid action " + bo.action);
		}
	}
	
	private void updateDoor(BuildObject buildObject) {
		BuildObject existing = find(buildObject.id, zone.name);
		if (existing == null) {
			return;
		}

		existing.doorStatus = buildObject.doorStatus;
		save(existing, characterId, characterId);

		GameMessage gameMessage = new GameMessage();
		gameMessage.buildObjects = new BuildObjects();
		gameMessage.buildObjects.action = BuildObjects.Action.PropUpdate;
		BuildObject reply = buildObject.clone();
		gameMessage.buildObjects.getBuildObjectList().add(reply);
		broadcast(gameMessage, existing.zone);
	}

	private void load() {
		List<BuildObjectDatas> datas = BuildObjectDatas.db().where("build_object_datas_zone = ?", zone.name);
		for (BuildObjectDatas data : datas) {
			byte[] bytes = Base64.decodeBase64(data.dataText);
			BuildObject buildObject = BuildObject.parseFrom(bytes);
			if (buildObject.state == BuildObject.State.Removed) {
				continue;
			}
			cache.objectIndex.put(buildObject.id, buildObject);
			setGridAndVitals(buildObject);
		}
		logger.warn(cache.objectIndex.size() + " build objects loaded for "+zone.name);
	}

	

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerDisconnect(String playerId) {

	}

}
