package io.gamemachine.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GameConfig;
import io.gamemachine.config.AppConfig.GridConfig;
import io.gamemachine.messages.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.messages.Character;

public class GameGrid {

	private static final Logger logger = LoggerFactory.getLogger(GameGrid.class);

	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>> gameGrids = new ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>>();
	private static ConcurrentHashMap<String, Integer> playerZone = new ConcurrentHashMap<String, Integer>();
	private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> zoneToGridName = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>>();
	
	
	public static int getPlayerZone(String playerId) {
		if (playerZone.containsKey(playerId)) {
			return playerZone.get(playerId);
		} else {
			int zone = 0;
			String characterId = PlayerService.getInstance().getCharacter(playerId);
			if (!Strings.isNullOrEmpty(characterId)) {
				Character character = CharacterService.getInstance().find(playerId, characterId);
				if (character.zone > 0) {
					zone = character.zone;
				}
				setPlayerZone(playerId,zone);
			}
			
			return zone;
		}
	}

	public static void setPlayerZone(String playerId, int zone) {
		playerZone.put(playerId, zone);
		
		// make sure player is removed from associated grid
		for (Grid grid : GameGrid.getGridList()) {
			grid.remove(playerId);
		}
				
		String characterId = PlayerService.getInstance().getCharacter(playerId);
		if (!Strings.isNullOrEmpty(characterId)) {
			Character character = CharacterService.getInstance().find(playerId, characterId);
			character.zone = zone;
			CharacterService.getInstance().save(character);
		}
	}

	public static void getGridCounts() {
		for (String gameId : gameGrids.keySet()) {
			Map<String, Grid> grids = gameGrids.get(gameId);
			for (Map.Entry<String, Grid> entry : grids.entrySet()) {
				Grid grid = entry.getValue();
				logger.debug("Grid " + gameId + ":" + entry.getKey() + " count " + grid.getObjectCount() + " max="
						+ grid.getMax() + " size=" + grid.getCellSize());
				// entry.getValue().dumpGrid();
			}
		}
	}

	public static List<Grid> getGridList() {
		List<Grid> allgrids = new ArrayList<Grid>();
		for (String gameId : gameGrids.keySet()) {
			Map<String, Grid> grids = gameGrids.get(gameId);
			for (Map.Entry<String, Grid> entry : grids.entrySet()) {
				Grid grid = entry.getValue();
				allgrids.add(grid);
			}
		}
		return allgrids;
	}

	public static List<Grid> gridsStartingWith(String name) {
		List<Grid> allgrids = new ArrayList<Grid>();
		for (String gameId : gameGrids.keySet()) {
			Map<String, Grid> grids = gameGrids.get(gameId);
			for (Map.Entry<String, Grid> entry : grids.entrySet()) {
				Grid grid = entry.getValue();
				if (grid.name.startsWith(name)) {
					allgrids.add(grid);
				}
			}
		}
		return allgrids;
	}
	
	public static Map<String, ConcurrentHashMap<String, Grid>> getGameGrids() {
		return gameGrids;
	}

	public static void removeGridsForGame(String gameId) {
		gameGrids.remove(gameId);
	}

	public static synchronized Grid loadGameGrid(String gameId, String gridName) {
		GridConfig config = GameConfig.getGridConfig(gameId, gridName);
		if (config == null) {
			return null;
		} else {
			return createGameGrid(gameId, config);
		}
	}

	public static synchronized Grid createGameGrid(String gameId, GridConfig config) {
		logger.debug("createGameGrid " + gameId + " " + config.getName());
		if (!gameGrids.containsKey(gameId)) {
			gameGrids.put(gameId, new ConcurrentHashMap<String, Grid>());
		}

		if (gameGrids.get(gameId).size() >= 50) {
			logger.info("Grid limit exceeded");
			return null;
		}

		Grid existing = gameGrids.get(gameId).get(config.getName());
		if (existing != null) {
			if (existing.getMax() == config.getGridSize() && existing.getCellSize() == config.getCellSize()) {
				logger.debug("existing grid with same settings " + config.getName());
				return existing;
			}
		}

		Grid gameGrid = new Grid(config.getName(), config.getGridSize(), config.getCellSize());
		gameGrids.get(gameId).put(config.getName(), gameGrid);
		logger.debug("Grid created for " + gameId + " " + config.getName());
		return gameGrid;
	}

	public static Grid xgetGameGrid(String gridName) {
		String gameId = AppConfig.getDefaultGameId();
		if (gameGrids.containsKey(gameId)) {
			if (gameGrids.get(gameId).containsKey(gridName)) {
				return gameGrids.get(gameId).get(gridName);
			} else {
				return loadGameGrid(gameId, gridName);
			}
		} else {
			return loadGameGrid(gameId, gridName);
		}
	}
	
	public static Grid xgetGameGrid(String gameId, String gridName) {
		if (gameGrids.containsKey(gameId)) {
			if (gameGrids.get(gameId).containsKey(gridName)) {
				return gameGrids.get(gameId).get(gridName);
			} else {
				return loadGameGrid(gameId, gridName);
			}
		} else {
			return loadGameGrid(gameId, gridName);
		}
	}

	public static Grid getGameGrid(String gameId, String gridName, int zone) {
		String zgridName;

		if (!zoneToGridName.containsKey(gridName)) {
			zoneToGridName.put(gridName, new ConcurrentHashMap<Integer, String>());
		}
		
		if (zoneToGridName.get(gridName).containsKey(zone)) {
			zgridName = zoneToGridName.get(gridName).get(zone);
		} else {
			zgridName = gridName + zone;
			zoneToGridName.get(gridName).put(zone, zgridName);
		}
		return xgetGameGrid(gameId, zgridName);
	}
	
	public static Grid getGameGrid(String gameId, String gridName, String playerId) {
		int zone = getPlayerZone(playerId);
		return getGameGrid(gameId,gridName,zone);
	}
	
	public static Grid getGameGrid(String gridName, String playerId) {
		String gameId = PlayerService.getInstance().getGameId(playerId);
		int zone = getPlayerZone(playerId);
		return getGameGrid(gameId,gridName,zone);
	}
}
