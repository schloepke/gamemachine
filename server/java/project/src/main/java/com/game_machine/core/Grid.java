package com.game_machine.core;

/*
 * Implements fast 2d spatial hashing.  Neighbor queries return all entities that are in our cell and neighboring cells.  The bounding is a box not a radius,
 * so there is no way to get all entities within an exact range.  This is normally not an issue for large numbers of entities in a large open space, and if you are
 * working with a small number of entities you can afford to do additional filtering client side.
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.CreateGrid;
import GameMachine.Messages.Entity;
import GameMachine.Messages.TrackData;
import GameMachine.Messages.TrackData.EntityType;

import com.game_machine.core.AppConfig.GridConfig;
import com.game_machine.objectdb.Store;

public class Grid {

	private int max;
	private int cellSize = 0;
	private float convFactor;
	private int width;
	private int cellCount;

	private MovementVerifier movementVerifier;

	private static final Logger logger = LoggerFactory.getLogger(Grid.class);

	public static ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>> gameGrids = new ConcurrentHashMap<String, ConcurrentHashMap<String, Grid>>();

	public static ConcurrentHashMap<String, Grid> grids = new ConcurrentHashMap<String, Grid>();

	private ConcurrentHashMap<String, TrackData> deltaIndex = new ConcurrentHashMap<String, TrackData>();
	private ConcurrentHashMap<String, TrackData> objectIndex = new ConcurrentHashMap<String, TrackData>();
	private ConcurrentHashMap<String, Integer> cellsIndex = new ConcurrentHashMap<String, Integer>();
	private ConcurrentHashMap<Integer, ConcurrentHashMap<String, TrackData>> cells = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, TrackData>>();
	private ConcurrentHashMap<Integer, Set<Integer>> cellsCache = new ConcurrentHashMap<Integer, Set<Integer>>();

	public static void resetGrids() {
		grids = new ConcurrentHashMap<String, Grid>();
	}

	public static synchronized Grid loadGameGrid(String gameId, String gridName) {
		logger.debug("loadGameGrid " + gameId + " " + gridName);
		Store store = Store.getInstance();
		String savedId = "create_grid_" + gameId + "_" + gridName;
		Entity entity = (Entity) store.get(savedId, Entity.class);
		if (entity == null) {
			logger.debug(savedId + " not found in store");
			String gridStr = AppConfig.Grids.getByName(gridName);
			if (gridStr != null) {
				GridConfig config = new GridConfig(gridName, gridStr);
				CreateGrid create = new CreateGrid();
				create.setName(gridName).setGridSize(config.getGridSize()).setCellSize(config.getCellSize());
				store.set(savedId, new Entity().setId(savedId).setCreateGrid(create));
				return createGameGrid(gameId, create);
			}
		} else {
			return createGameGrid(gameId, entity.getCreateGrid());
		}
		return null;
	}

	public static synchronized Grid createGameGrid(String gameId, CreateGrid createGrid) {
		logger.debug("createGameGrid " + gameId + " " + createGrid.getName());
		if (!gameGrids.containsKey(gameId)) {
			gameGrids.put(gameId, new ConcurrentHashMap<String, Grid>());
		}

		if (gameGrids.get(gameId).size() >= 5) {
			logger.info("Grid limit exceeded");
			return null;
		}

		Grid existing = gameGrids.get(gameId).get(createGrid.getName());
		if (existing != null) {
			if (existing.max == createGrid.getGridSize() && existing.cellSize == createGrid.getCellSize()) {
				logger.debug("existing grid with same settings " + createGrid.getName());
				return existing;
			}
		}

		Grid gameGrid = Grid.findOrCreate(gameId, createGrid.getGridSize(), createGrid.getCellSize());
		gameGrids.get(gameId).put(createGrid.getName(), gameGrid);
		logger.debug("Grid created for " + gameId + " " + createGrid.getName());
		return gameGrid;
	}

	public static Grid getGameGrid(String gameId, String gridName) {
		if (gameGrids.containsKey(gameId)) {
			if (gameGrids.get(gameId).containsKey(gridName)) {
				return gameGrids.get(gameId).get(gridName);
			} else {
				loadGameGrid(gameId, gridName);
				return gameGrids.get(gameId).get(gridName);
			}
		} else {
			// No grids created for this gameId, create the defaults
			loadGameGrid(gameId, gridName);
			return gameGrids.get(gameId).get(gridName);
		}
	}

	public static synchronized Grid findOrCreate(String name, int gridSize, int cellSize) {
		if (grids.containsKey(name)) {
			return grids.get(name);
		} else {
			Grid grid = new Grid(gridSize, cellSize);
			grids.put(name, grid);
			return grid;
		}
	}

	public static Grid find(String name) {
		if (grids.containsKey(name)) {
			return grids.get(name);
		} else {
			return null;
		}
	}

	public Grid(int max, int cellSize) {
		this.max = max;
		this.cellSize = cellSize;
		this.convFactor = 1.0f / this.cellSize;
		this.width = (int) (this.max / this.cellSize);
		this.cellCount = this.width * this.width;
	}

	public void setMovementVerifier(MovementVerifier movementVerifier) {
		this.movementVerifier = movementVerifier;
	}

	public int getMax() {
		return this.max;
	}

	public int getCellSize() {
		return this.cellSize;
	}

	public int getWidth() {
		return this.width;
	}

	public int getCellCount() {
		return this.cellCount;
	}

	public Set<Integer> cellsWithinBounds(float x, float y) {
		Set<Integer> cells = new HashSet<Integer>();

		int offset = this.cellSize;

		int startX = (int) (x - offset);
		int startY = (int) (y - offset);
		
		// subtract one from offset to keep it from hashing to the next cell boundary outside of range
		int endX = (int) (x + offset-1);
		int endY = (int) (y + offset-1);

		for (int rowNum = startX; rowNum <= endX; rowNum += offset) {
			for (int colNum = startY; colNum <= endY; colNum += offset) {
				if (rowNum >= 0 && colNum >= 0) {
					cells.add(hash(rowNum, colNum));
				}
			}
		}
		return cells;
	}

	public ArrayList<TrackData> neighbors(float x, float y, EntityType entityType) {
		ArrayList<TrackData> result;

		Collection<TrackData> gridValues;
		result = new ArrayList<TrackData>();
		Set<Integer> cells = cellsWithinBounds(x, y);
		for (int cell : cells) {
			gridValues = gridValuesInCell(cell);
			if (gridValues == null) {
				continue;
			}
			
			for (TrackData gridValue : gridValues) {
				if (gridValue != null) {
					if (entityType == null) {
						result.add(gridValue);
					} else if (gridValue.entityType == entityType) {
						result.add(gridValue);
					}
				}
			}
		}
		return result;
	}

	public Collection<TrackData> gridValuesInCell(int cell) {
		ConcurrentHashMap<String, TrackData> cellGridValues = cells.get(cell);

		if (cellGridValues != null) {
			return cellGridValues.values();
		} else {
			return null;
		}
	}

	public void updateFromDelta(TrackData[] gridValues) {
		for (TrackData gridValue : gridValues) {
			if (gridValue != null) {
				objectIndex.put(gridValue.id, gridValue);
			}
		}
	}

	public TrackData[] currentDelta() {
		TrackData[] a = new TrackData[deltaIndex.size()];
		deltaIndex.values().toArray(a);
		deltaIndex.clear();
		return a;
	}

	public ArrayList<TrackData> getNeighborsFor(String id, EntityType entityType) {
		TrackData gridValue = get(id);
		if (gridValue == null) {
			return null;
		}
		return neighbors(gridValue.x, gridValue.y, entityType);
	}

	public List<TrackData> getAll() {
		return new ArrayList<TrackData>(objectIndex.values());
	}

	public TrackData get(String id) {
		return objectIndex.get(id);
	}

	public void remove(String id) {
		TrackData indexValue = objectIndex.get(id);
		if (indexValue != null) {
			int cell = cellsIndex.get(id);
			ConcurrentHashMap<String, TrackData> cellGridValues = cells.get(cell);
			if (cellGridValues != null) {
				cellGridValues.remove(id);
			}
			objectIndex.remove(id);
			cellsIndex.remove(id);
		}
	}

	public Boolean set(String id, float x, float y, float z, EntityType entityType) {
		TrackData trackData = new TrackData();
		trackData.id = id;
		trackData.x = x;
		trackData.y = y;
		trackData.z = z;
		trackData.entityType = entityType;
		return set(trackData);
	}

	public Boolean set(TrackData trackData) {

		if (trackData.entityType == EntityType.PLAYER) {
			if (movementVerifier != null) {
				if (!movementVerifier.verify(trackData)) {
					return false;
				}
			}
		}

		Boolean hasExisting = false;
		Integer oldCellValue = -1;
		String id = trackData.id;

		if (objectIndex.containsKey(id)) {
			hasExisting = true;
			oldCellValue = cellsIndex.get(id);
		}

		int cell = hash(trackData.x, trackData.y);

		if (hasExisting) {
			if (oldCellValue != cell) {
				ConcurrentHashMap<String, TrackData> cellGridValues = cells.get(oldCellValue);
				cellGridValues.remove(id);
				if (cellGridValues.size() == 0) {
					cells.remove(oldCellValue);
				}

			}
			objectIndex.replace(id, trackData);
			cellsIndex.replace(id, cell);
		} else {
			cellsIndex.put(id, cell);
			objectIndex.put(id, trackData);
		}

		if (!cells.containsKey(cell)) {
			cells.put(cell, new ConcurrentHashMap<String, TrackData>());
		}
		cells.get(cell).put(id, trackData);

		// deltaIndex.put(id, gridValue);

		return true;
	}

	public int hash2(float x, float y) {
		return (int) (Math.floor(x / this.cellSize) + Math.floor(y / this.cellSize) * width);
	}

	public int hash(float x, float y) {
		return (int) ((x * this.convFactor)) + (int) ((y * this.convFactor)) * this.width;
	}
}
