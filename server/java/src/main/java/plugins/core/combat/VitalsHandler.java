package plugins.core.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.Commands;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;

public class VitalsHandler extends GameMessageActor {

	public static String name = VitalsHandler.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(VitalsHandler.class);
	private static ConcurrentHashMap<String, VitalsProxy> proxies = new ConcurrentHashMap<String, VitalsProxy>();

	public static void remove(String id) {
		if (proxies.containsKey(id)) {
			proxies.remove(id);
		}
	}

	public static Collection<VitalsProxy> getVitals() {
		return proxies.values();
	}
	
	public static List<VitalsProxy> getVitalsForZone(String zone) {
		List<VitalsProxy> zoneVitals = new ArrayList<VitalsProxy>();
		for (VitalsProxy proxy : proxies.values()) {
			if (proxy.vitals.zoneName.equals(zone)) {
				zoneVitals.add(proxy);
			}
		}
		return zoneVitals;
	}

	public static VitalsProxy fromTrackData(TrackData trackData, String zone) {
		if (trackData.entityType == TrackData.EntityType.Object) {
			return get(trackData.id, Vitals.VitalsType.Object, zone);
		} else if (trackData.entityType == TrackData.EntityType.BuildObject) {
			return get(trackData.id, Vitals.VitalsType.BuildObject, zone);
		} else if (trackData.entityType == TrackData.EntityType.Player
				|| trackData.entityType == TrackData.EntityType.Npc) {
			return get(trackData.id, zone);
		} else {
			throw new RuntimeException("Can't get vitals for entity type " + trackData.entityType.toString());
		}
	}

	public static void ensure(String entityId, String zone) {
		if (!proxies.containsKey(entityId)) {
			Player player = PlayerService.getInstance().find(entityId);

			GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();

			Vitals vitals = gameEntityManager.getBaseVitals(player.characterId);
			vitals.characterId = player.characterId;
			vitals.entityId = player.id;
			vitals.zoneName = zone;
			VitalsProxy proxy = new VitalsProxy(vitals);
			proxies.put(vitals.entityId, proxy);
		}
	}

	public static void ensure(String entityId, Vitals.VitalsType vitalsType, String zone) {
		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();

		if (!proxies.containsKey(entityId)) {
			Vitals vitals = gameEntityManager.getBaseVitals(entityId, vitalsType);
			vitals.zoneName = zone;
			vitals.entityId = entityId;
			VitalsProxy proxy = new VitalsProxy(vitals);
			proxies.put(vitals.entityId, proxy);
		}
	}

	public static VitalsProxy get(String entityId) {
		return proxies.get(entityId);
	}
	
	public static VitalsProxy get(String entityId, String zone) {
		ensure(entityId, zone);
		return proxies.get(entityId);
	}

	public static VitalsProxy get(String entityId, Vitals.VitalsType vitalsType, String zone) {
		ensure(entityId, vitalsType, zone);
		return proxies.get(entityId);
	}

	@Override
	public void awake() {
		Commands.clientManagerRegister(name);
		scheduleOnce(5000L, "vitals");
	}

	@Override
	public void onTick(String message) {
		scheduleOnce(5000L, "vitals");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub

	}

	public void onPlayerDisconnect(String playerId) {
		logger.warn("PlayerDisconnect " + playerId);
		VitalsHandler.remove(playerId);
	}
}
