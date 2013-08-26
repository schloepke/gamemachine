package com.game_machine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Grid {

	private float max;
	private int cellSize = 0;
	private float convFactor;
	private int width;
	private int cellCount;
	private ConcurrentHashMap<String, GridValue> objectIndex = new ConcurrentHashMap<String, GridValue>();
	private ConcurrentHashMap<Integer, ConcurrentHashMap<String, GridValue>> cells = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, GridValue>>();
	private ConcurrentHashMap<Integer, Set<Integer>> cellsCache = new ConcurrentHashMap<Integer, Set<Integer>>();

	public Grid(int max, int cellSize) {
		this.max = max;
		this.cellSize = cellSize;
		this.convFactor = 1.0f / this.cellSize;
		this.width = (int) (this.max / this.cellSize);
		this.cellCount = this.width * this.width;
	}

	public Set<Integer> cellsWithinRadius(float x, float y, int radius) {
		int cellHash = hash(x,y);
		int key = cellHash + radius;
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

		for (int rowNum = startX; rowNum <= endX; rowNum+=this.cellSize) {
			for (int colNum = startY; colNum <= endY; colNum+=this.cellSize) {
				if (rowNum >= 0 && colNum >= 0) {
					cells.add(hash(rowNum,colNum));
				}
			}
		}
		cellsCache.put(key, cells);
		return cells;
	}

	public ArrayList<GridValue> neighbors(float x, float y, int radius, String entityType, String callerId) {
		Collection<GridValue> gridValues;
		ArrayList<GridValue> result = new ArrayList<GridValue>();
		Set<Integer> cells = cellsWithinRadius(x, y, radius);
		for (int cell : cells) {
			gridValues = gridValuesInCell(cell);
			if (gridValues != null) {
				for (GridValue gridValue : gridValues) {
					if (entityType == null) {
						result.add(gridValue);
					} else if (gridValue.entityType.equals(entityType)) {
						result.add(gridValue);
					}
				}
			}
		}
		return result;
	}

	public Collection<GridValue> gridValuesInCell(int cell) {
		ConcurrentHashMap<String, GridValue> points = cells.get(cell);
		if (points != null) {
			return points.values();
		} else {
			return null;
		}
	}

	public GridValue get(String id) {
		return objectIndex.get(id);
	}

	public void remove(String id) {
		GridValue indexValue = objectIndex.get(id);
		if (indexValue != null) {
			int cell = indexValue.cell;
			ConcurrentHashMap<String, GridValue> cellValue = cells.get(cell);
			if (cellValue != null) {
				cellValue.remove(id);
			}
			objectIndex.remove(id);
		}
	}

	public Boolean set(String id, float x, float y, float z, String entityType) {
		GridValue oldValue = objectIndex.get(id);

		if (oldValue != null) {
			if (oldValue.x == x && oldValue.y == y) {
				return false;
			}
		}

		int cell = hash(x, y);
		if (!cells.containsKey(cell)) {
			cells.put(cell, new ConcurrentHashMap<String, GridValue>());
		}

		GridValue gridValue;
		gridValue = new GridValue(id, cell, x, y, z, entityType);

		if (oldValue != null && oldValue.cell != cell) {
			cells.get(oldValue.cell).remove(id);
		}
		cells.get(cell).put(id, gridValue);

		objectIndex.put(id, gridValue);

		return true;
	}

	public int hash2(float x, float y) {
		return (int) (Math.floor(x / this.cellSize) + Math.floor(y / this.cellSize) * width);
	}

	public int hash(float x, float y) {
		return (int) ((x * this.convFactor)) + (int) ((y * this.convFactor)) * this.width;
	}
}
