package io.gamemachine.regions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.RegionInfo;
import io.gamemachine.messages.Zone;
import io.gamemachine.messages.Zones;

public class ZoneService extends GameMessageActor {

	public static String name = ZoneService.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(ZoneService.class);
	private static PriorityBlockingQueue<Integer> zoneQueue = new PriorityBlockingQueue<Integer>();
	private static Map<String, Zone> zones = new ConcurrentHashMap<String, Zone>();
	private static Map<Integer, String> numberToName = new ConcurrentHashMap<Integer, String>();
	private static Map<String,String> nameToRegion = new ConcurrentHashMap<String,String>();
	
	private static int maxZones = 0;
	
	private static String defaultZoneName = null;
	private static RegionInfo region = null;
	private Long updateInterval = 10000l;

	public static List<Zone> staticZones() {
		List<Zone> defaults = new ArrayList<Zone>();
		for (String name : nameToRegion.keySet()) {
			defaults.add(getZone(name));
		}
		return defaults;
	}

	public static Zones getPlayerZones(String playerId) {
		Zones playerZones = new Zones();
		playerZones.current = PlayerService.getInstance().getZone(playerId);
		playerZones.setZoneList(new ArrayList<Zone>(zones.values()));
		for (Zone z : zones.values()) {
			if (z.isPublic) {
				playerZones.addZone(z);
			} else if (z.getPlayerIdsList().contains(playerId)) {
				playerZones.addZone(z);
			}
			
		}
		return playerZones;
	}
	
	public static Zone defaultZone() {
		return getZone(defaultZoneName);
	}

	public static void releaseZone(String name) {
		if (zones.containsKey(name)) {
			Zone zone = zones.get(name);
			zoneQueue.put(zone.number);
			zones.remove(name);
			numberToName.remove(zone.number);
		}
	}

	public static Zone getZone(int number) {
		if (numberToName.containsKey(number)) {
			return getZone(numberToName.get(number));
		} else {
			throw new RuntimeException("Zone not created " + number);
		}

	}

	public static Zone getZone(String name) {
		return getZone(name,true,null);
	}
	
	public static Zone getPrivateZone(String name) {
		return getZone(name,true,null);
	}
	
	public static Zone getPrivateZone(String name, List<String> playerIds) {
		return getZone(name,true, playerIds);
	}
	
	public static Zone getZone(String name, boolean isPublic, List<String> playerIds) {
		if (zones.containsKey(name)) {
			Zone zone = zones.get(name);
			if (zone.isPublic || zone.isPublic == isPublic) {
				return zone;
			} else {
				return null;
			}
		} else {
			try {
				Integer number = zoneQueue.poll(10, TimeUnit.MILLISECONDS);
				if (number == null) {
					logger.warn("Unable to get zone from queue");
					return null;
				}
				Zone zone = new Zone();
				
				zone.name = name;
				zone.number = number;
				zone.isPublic = isPublic;
				zone.setPlayerIdsList(playerIds);
				setRegion(zone);
				zones.put(name, zone);
				numberToName.put(number, zone.name);
				return zone;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static void init() {
		region = getRegion();

		Config config = AppConfig.getConfig();
		defaultZoneName = config.getString("gamemachine.default_zone");

		maxZones = config.getInt("gamemachine.max_zones");

		zoneQueue.clear();
		for (int i = 0; i < maxZones; i++) {
			zoneQueue.put(i);
		}

		List<? extends Config> values = config.getConfigList("gamemachine.zones");
		for (Config value : values) {
			String zoneName = value.getString("name");
			String region = value.getString("region");
			nameToRegion.put(zoneName,region);
			getZone(zoneName);
		}
	}

	private static RegionInfo getRegion(String name) {
		return RegionInfo.db().findFirst("region_info_id = ?", name);
	}
	
	private static RegionInfo getRegion() {
		String myAddress = ActorUtil.selfAddress();
		for (RegionInfo info : RegionInfo.db().findAll()) {
			if (info.assigned && info.node.equals(myAddress)) {
				return info;
			}
		}
		return null;
	}

	public void onTick(String message) {
		updateStatus();
		scheduleOnce(updateInterval, "tick");
	}

	private static void setRegion(Zone zone) {
		if (nameToRegion.containsKey(zone.name)) {
			zone.region = nameToRegion.get(zone.name);
			RegionInfo info = getRegion(zone.region);
			if (info != null) {
				zone.hostname = info.hostname;
			}
		} else if (region != null) {
			zone.region = region.id;
		}
	}
	
	private void updateStatus() {
		region = getRegion();
		for (Zone zone : zones.values()) {
			setRegion(zone);
		}
	}

	@Override
	public void awake() {
		scheduleOnce(updateInterval, "tick");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {

	}
}
