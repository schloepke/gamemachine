package com.game_machine.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.config.GameConfig;
import com.game_machine.config.AppConfig.GridConfig;

public class GameGrid {

	private static final Logger logger = LoggerFactory.getLogger(GameGrid.class);

	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>> gameGrids = new ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>>();
	
	public static void getGridCounts() {
		for (String gameId : gameGrids.keySet()) {
			Map<String, Grid> grids = gameGrids.get(gameId);
			for (Map.Entry<String, Grid> entry : grids.entrySet())
			{
				logger.info("Grid "+gameId+":"+entry.getKey()+" count "+entry.getValue().getObjectCount());
			}
		}
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

		if (gameGrids.get(gameId).size() >= 5) {
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

		Grid gameGrid = new Grid(config.getGridSize(), config.getCellSize());
		gameGrids.get(gameId).put(config.getName(), gameGrid);
		logger.debug("Grid created for " + gameId + " " + config.getName());
		return gameGrid;
	}

	public static Grid getGameGrid(String gameId, String gridName) {
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
}
