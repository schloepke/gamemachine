package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import user.messages.GameEntity;
import io.gamemachine.client.messages.TrackData;

import io.gamemachine.client.api.ClientGrid;

public class Globals {

	private static ConcurrentHashMap<String,GameEntity> gameEntities = new ConcurrentHashMap<String,GameEntity>();
	public static final ClientGrid grid = ClientGrid.findOrCreate("default", 400, 50);
	public static final ClientGrid aoeGrid = ClientGrid.findOrCreate("aoe", 2000, 10);
	
	public static void clearVitals() {
		gameEntities.clear();
	}
	
	public static Map<String,GameEntity> getVitals() {
		return gameEntities;
	}
	
	public static List<String> getPlayerIds() {
		List<String> playerIds = new ArrayList<String>();
		for (GameEntity v : gameEntities.values()) {
			if (v.entityType == TrackData.EntityType.PLAYER) {
				playerIds.add(v.id);
			}
		}
		return playerIds;
	}
	
	public static List<GameEntity> getPlayerEntities() {
		List<GameEntity> playerIds = new ArrayList<GameEntity>();
		for (GameEntity v : gameEntities.values()) {
			if (v.entityType == TrackData.EntityType.PLAYER) {
				playerIds.add(v);
			}
		}
		return playerIds;
	}
	
	public static List<GameEntity> gameEntitiesList() {
		return new ArrayList<GameEntity>(gameEntities.values());
	}
	
	public static GameEntity gameEntityFor(String id) {
		if (gameEntities.containsKey(id)) {
			return gameEntities.get(id);
		} else {
			return null;
		}
	}

	public static boolean hasGameEntity(String id) {
		return gameEntities.containsKey(id);
	}
	
	public static void setGameEntity(GameEntity gameEntity) {
		Globals.gameEntities.put(gameEntity.id, gameEntity);
	}
	
	public static void removeVitalsFor(String id) {
		gameEntities.remove(id);
	}

}
