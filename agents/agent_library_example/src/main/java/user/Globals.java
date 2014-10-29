package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import user.messages.Vitals;

import com.game_machine.client.api.Api;
import com.game_machine.client.api.ClientGrid;

public class Globals {

	private static ConcurrentHashMap<String,Vitals> vitals = new ConcurrentHashMap<String,Vitals>();
	public static final ClientGrid grid = ClientGrid.findOrCreate("default", 4000, 50);
	public static final ClientGrid aoeGrid = ClientGrid.findOrCreate("aoe", 4000, 10);
	
	public static Map<String,Vitals> getVitals() {
		return vitals;
	}
	
	public static List<String> getPlayerIds() {
		List<String> playerIds = new ArrayList<String>();
		for (Vitals v : vitals.values()) {
			if (v.entityType.equals("player")) {
				playerIds.add(v.id);
			}
		}
		return playerIds;
	}
	
	public static List<Vitals> getVitalsList() {
		return new ArrayList<Vitals>(vitals.values());
	}
	
	public static Vitals getVitalsFor(String id) {
		if (vitals.containsKey(id)) {
			return vitals.get(id);
		} else {
			return null;
		}
	}

	public static boolean hasVitalsFor(String id) {
		return vitals.containsKey(id);
	}
	
	public static void setVitalsFor(Vitals vitals) {
		Globals.vitals.put(vitals.id, vitals);
	}
	
	public static void removeVitalsFor(String id) {
		vitals.remove(id);
	}

}
