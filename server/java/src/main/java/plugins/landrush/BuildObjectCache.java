package plugins.landrush;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.Zone;
import io.gamemachine.objectdb.Cache;

public class BuildObjectCache {

	public String zone;
	public Map<String, BuildObject> objectIndex = new ConcurrentHashMap<String, BuildObject>();
	public AtomicInteger updateCount = new AtomicInteger();
	public Cache<String, Integer> versionCache = new Cache<String, Integer>(60, 10000);
	public Map<String, Map<String, Integer>> playerVersions = new ConcurrentHashMap<String, Map<String, Integer>>();
	
	private static Map<String,BuildObjectCache> zones = new ConcurrentHashMap<String,BuildObjectCache>();
	
	public BuildObjectCache(String zone) {
		this.zone = zone;
	}
	
	public static BuildObjectCache get(String zone) {
		if (zones.containsKey(zone)) {
			return zones.get(zone);
		} else {
			BuildObjectCache cache = new BuildObjectCache(zone);
			zones.put(zone, cache);
			return cache;
		}
	}
}
