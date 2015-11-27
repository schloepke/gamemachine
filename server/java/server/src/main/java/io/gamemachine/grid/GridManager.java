package io.gamemachine.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GridConfig;
import io.gamemachine.config.GridConfigs;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;

public class GridManager {

	private static final Logger logger = LoggerFactory.getLogger(GridManager.class);

	private static ConcurrentHashMap<Integer, ConcurrentHashMap<String, Grid>> gameGrids = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, Grid>>();
	private static ConcurrentHashMap<String, Integer> playerZone = new ConcurrentHashMap<String, Integer>();

	public static int getEntityZone(String entityId) {
		if (playerZone.containsKey(entityId)) {
			return playerZone.get(entityId);
		} else {
			int zone = 0;
			String characterId = PlayerService.getInstance().getCharacter(entityId);
			if (!Strings.isNullOrEmpty(characterId)) {
				Character character = CharacterService.instance().find(entityId, characterId);
				if (character.zone > 0) {
					zone = character.zone;
				}
				setEntityZone(entityId, zone);
			}

			return zone;
		}
	}

	public static void removeEntityFromZones(String entityId) {
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grid.remove(entityId);
			}
		}
	}
	
	public static void removeExpired() {
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grid.RemoveExpired(AppConfig.getGridExpiration());
			}
		}
	}
	
	public static List<Grid> getGrids() {
		List<Grid> grids = new ArrayList<Grid>();
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grids.add(grid);
			}
		}
		return grids;
	}
	
	public static void setEntityZone(String entityId, int zone) {
		playerZone.put(entityId, zone);

		removeEntityFromZones(entityId);
		
		String characterId = PlayerService.getInstance().getCharacter(entityId);
		if (!Strings.isNullOrEmpty(characterId)) {
			Character character = CharacterService.instance().find(entityId, characterId);
			character.zone = zone;
			CharacterService.instance().save(character);
		} else {
			logger.warn("Unable to find character id for entityId "+entityId);
		}
	}

	public static synchronized Grid createGrid(int zone, String name) {
		if (!gameGrids.containsKey(zone)) {
			gameGrids.put(zone, new ConcurrentHashMap<String,Grid>());
		}
		
		Map<String, Grid> grids = gameGrids.get(zone);
		if (!grids.containsKey(name)) {
			GridConfig gridConfig = GridConfigs.getGridConfig(name);
			Grid grid = Grid.createFromConfig(gridConfig, zone);
			grids.put(name, grid);
		}
		
		return gameGrids.get(zone).get(name);
	}
	
	public static Grid getGrid(int zone, String name) {
		if (Strings.isNullOrEmpty(name)) {
			name = "default";
		}
		
		if (!gameGrids.containsKey(zone)) {
			return createGrid(zone, name);
		}
		
		Map<String, Grid> grids = gameGrids.get(zone);
		if (!grids.containsKey(name)) {
			return createGrid(zone, name);
		}
		
		return gameGrids.get(zone).get(name);
	}

	public static Grid getPlayerGrid(String name, String playerId) {
		int zone = getEntityZone(playerId);
		return getGrid(zone,name);
	}
	
}
