package plugins.zonemanager;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Zone;
import io.gamemachine.messages.ZoneInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ZoneHandler extends GameMessageActor {

	public static String name = ZoneHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private Map<String, Zone.Status> statuses = new HashMap<String, Zone.Status>();
	private static Map<String, Zone> zones = new HashMap<String, Zone>();

	
	public static String getUnityClientForZone(String name) {
		if (zones.containsKey(name)) {
			return zones.get(name).unityClient;
		} else {
			return null;
		}
	}
	
	@Override
	public void awake() {
		scheduleOnce(10000l, "tick");

	}

	@Override
	public void onTick(String message) {
		updateStatus();
		scheduleOnce(10000l, "tick");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.zone != null) {
				for (Zone zone : zones.values()) {
					if (zone.unityClient.equals(playerId)) {
						gameMessage.zone = zone;
						setReply(gameMessage);
						return;
					}
				}
				
				for (Zone zone : zones.values()) {
					if (!Strings.isNullOrEmpty(zone.unityClient)) {
						zone.unityClient = playerId;
						gameMessage.zone = zone;
						logger.warning("Zone "+zone.name+" assigned to unityClient "+zone.unityClient);
						setReply(gameMessage);
						return;
					}
				}
			}
			
			setReply(gameMessage);
		}
	}

	private void stopZone(String name) {
		if (zones.containsKey(name)) {
			zones.remove(name);
		}
		changeStatus(name, Zone.Status.DOWN);
	}

	public void startZone(String name) {
		Zone zone = new Zone();
		zone.name = name;
		zone.status = Zone.Status.UP;
		zones.put(name, zone);
		changeStatus(name, Zone.Status.UP);
	}

	private void changeStatus(String zone, Zone.Status newStatus) {
		Zone.Status current;
		if (!statuses.containsKey(zone)) {
			current = Zone.Status.NONE;
		} else {
			current = statuses.get(zone);
		}
		logger.warning("Zone " + zone + " status " + current.toString() + " -> " + newStatus.toString());
		statuses.put(zone, newStatus);

	}

	private void updateStatus() {
		String myAddress = ActorUtil.selfAddress();
		List<ZoneInfo> infos = ZoneInfo.db().findAll();

		for (ZoneInfo info : infos) {
			if (info.assigned && info.node.equals(myAddress)) {
				if (!statuses.containsKey(info.id)) {
					changeStatus(info.id, Zone.Status.REQUEST_UP);
					startZone(info.id);
				} else {
					Zone.Status status = statuses.get(info.id);
					if (status != Zone.Status.UP && status != Zone.Status.REQUEST_UP) {
						changeStatus(info.id, Zone.Status.REQUEST_UP);
						startZone(info.id);
					}
				}
			}
		}

		for (String zoneName : statuses.keySet()) {
			boolean found = false;
			for (ZoneInfo info : infos) {
				if (info.assigned && info.node.equals(myAddress)) {
					found = true;
				}
			}
			if (!found) {
				Zone.Status status = statuses.get(zoneName);
				if (status != Zone.Status.DOWN && status != Zone.Status.REQUEST_DOWN) {
					changeStatus(zoneName, Zone.Status.REQUEST_DOWN);
					stopZone(zoneName);
				}
			}
		}
	}

}
