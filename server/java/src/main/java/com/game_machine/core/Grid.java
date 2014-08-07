package com.game_machine.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.TrackData;

public class Grid {

	private float max;
	private int cellSize = 0;
	private float convFactor;
	private int width;
	private int cellCount;

	private MovementVerifier movementVerifier;
	
	public static ConcurrentHashMap<String, Grid> grids = new ConcurrentHashMap<String, Grid>();
	
	private ConcurrentHashMap<String, TrackData> deltaIndex = new ConcurrentHashMap<String, TrackData>();
	private ConcurrentHashMap<String, TrackData> objectIndex = new ConcurrentHashMap<String, TrackData>();
	private ConcurrentHashMap<String, Integer> cellsIndex = new ConcurrentHashMap<String, Integer>();
	private ConcurrentHashMap<Integer, ConcurrentHashMap<String, TrackData>> cells = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, TrackData>>();
	private ConcurrentHashMap<Integer, Set<Integer>> cellsCache = new ConcurrentHashMap<Integer, Set<Integer>>();

	public static void resetGrids()
	{
		grids = new ConcurrentHashMap<String, Grid>();
	}
	
	public static synchronized Grid findOrCreate(String name, int gridSize, int cellSize) {
		if (grids.containsKey(name)) {
			return grids.get(name);
		} else {
			Grid grid = new Grid(gridSize,cellSize);
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
	
	public int getWidth() {
		return this.width;
	}

	public int getCellCount() {
		return this.cellCount;
	}

	public Set<Integer> cellsWithinRadius(float x, float y) {
		int cellHash = hash(x, y);
		return cellsWithinRadius(cellHash, x, y);
	}

	public Set<Integer> cellsWithinRadius(int cellHash, float x, float y) {
		int key = cellHash;
		Set<Integer> cells = cellsCache.get(key);
		if (cells != null) {
			return cells;
		}
		cells = new HashSet<Integer>();

		int offset = this.cellSize;

		int startX = (int) (x - offset);
		int startY = (int) (y - offset);
		int endX = (int) (x + offset);
		int endY = (int) (y + offset);

		for (int rowNum = startX; rowNum <= endX; rowNum += this.cellSize) {
			for (int colNum = startY; colNum <= endY; colNum += this.cellSize) {
				if (rowNum >= 0 && colNum >= 0) {
					cells.add(hash(rowNum, colNum));
				}
			}
		}
		cellsCache.put(key, cells);
		return cells;
	}

	public ArrayList<TrackData> neighbors(float x, float y, String entityType) {
		int myCell = hash(x, y);
		return neighbors(myCell, x, y, entityType);
	}

	// This could be optimized more (and gridValuesInCell), but it's simply dwarfed by
	// the overhead of serialization that at this point it's not really worth it.
	// - entityType should be an integer
	// - where we call gridValuesInCell, filter out by entity type there.
	// - and then just concat the return values of gridValuesInCell instead of building
	//   result one item at a time
	public ArrayList<TrackData> neighbors(int myCell, float x, float y, String entityType) {
		ArrayList<TrackData> result;

		TrackData[] gridValues;
		result = new ArrayList<TrackData>();
		Set<Integer> cells = cellsWithinRadius(myCell, x, y);
		for (int cell : cells) {
			gridValues = gridValuesInCell(cell);
			if (gridValues != null) {
				for (TrackData gridValue : gridValues) {
					if (gridValue != null) {
						if (entityType == null) {
							result.add(gridValue);
						} else if (gridValue.entityType.equals(entityType)) {
							result.add(gridValue);
						}
					}
				}
			}
		}
		return result;
	}

	public TrackData[] gridValuesInCell(int cell) {
		ConcurrentHashMap<String, TrackData> cellGridValues = cells.get(cell);

		if (cellGridValues != null) {
			TrackData[] a = new TrackData[cellGridValues.size()];
			cellGridValues.values().toArray(a);
			return a;
			// return cellGridValues.values();
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

	public ArrayList<TrackData> getNeighborsFor(String id, String entityType) {
		TrackData gridValue = get(id);
		if (gridValue == null)
		{
			return null;
		}
		return neighbors(gridValue.x, gridValue.y, entityType);
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

	public Boolean set(String id, float x, float y, float z, String entityType) {
		TrackData trackData = new TrackData();
		trackData.id = id;
		trackData.x = x;
		trackData.y = y;
		trackData.z = z;
		trackData.entityType = entityType;
		return set(trackData);
	}
	
	public Boolean set(TrackData trackData) {
		
		if (trackData.entityType.equals("player")) {
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

		//deltaIndex.put(id, gridValue);

		return true;
	}

	public int hash2(float x, float y) {
		return (int) (Math.floor(x / this.cellSize) + Math.floor(y / this.cellSize) * width);
	}

	public int hash(float x, float y) {
		return (int) ((x * this.convFactor)) + (int) ((y * this.convFactor)) * this.width;
	}
}
