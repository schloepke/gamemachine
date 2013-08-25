package com.game_machine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.Vector3;

public class Grid {

	private int max = 0;
	private int min = 0;
	private int cellSize = 0;
	private float convFactor;
	private int width;
	private int cellCount;
	private ConcurrentHashMap<String, GridValue> objectIndex = new ConcurrentHashMap<String, GridValue>();
	private ConcurrentHashMap<Integer, ConcurrentHashMap<String, GridValue>> cells = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, GridValue>>();
	private ConcurrentHashMap<String, Set<Integer>> cellsCache = new ConcurrentHashMap<String, Set<Integer>>();

	public Grid(int max, int cellSize) {
		this.max = max;
		this.cellSize = cellSize;
		this.convFactor = 1.0f / this.cellSize;
		this.width = this.max / this.cellSize;
		this.cellCount = this.width * this.width;
	}

	public void addIfWithinBounds(Set<Integer> cells, int cell) {
		if (cell >= this.min && cell <= (this.cellCount - 1)) {
			cells.add(cell);
		}
	}

	public Set<Integer> cellsWithinRadius(float x, float y, int radius) {
		String key = String.valueOf(x) + String.valueOf(y)
				+ String.valueOf(radius);
		Set<Integer> cells = cellsCache.get(key);
		if (cells != null) {
			return cells;
		}
		cells = new HashSet<Integer>();

		int offset = radius / this.cellSize;
		cells.add(hash(x, y));
		int bounds;
		for (int i = 1; i <= offset; i++) {
			bounds = radius * i;
			addIfWithinBounds(cells, hash(x - bounds, y - bounds));
			addIfWithinBounds(cells, hash(x - bounds, y + bounds));

			addIfWithinBounds(cells, hash(x + bounds, y - bounds));
			addIfWithinBounds(cells, hash(x + bounds, y + bounds));

			addIfWithinBounds(cells, hash(x - bounds, y));
			addIfWithinBounds(cells, hash(x + bounds, y));

			addIfWithinBounds(cells, hash(x, y - bounds));
			addIfWithinBounds(cells, hash(x, y + bounds));
		}
		cellsCache.put(key, cells);
		return cells;
	}

	public ArrayList<GridValue> neighbors(float x, float y, int radius,
			String entityType) {
		Collection<GridValue> points;
		ArrayList<GridValue> result = new ArrayList<GridValue>();
		Set<Integer> cells = cellsWithinRadius(x, y, radius);
		for (int cell : cells) {
			points = pointsInCell(cell);
			if (points != null) {
				for (GridValue point : points) {
					if (entityType == null) {
						result.add(point);
					} else if (point.entityType == entityType) {
						result.add(point);
					}
				}
			}
		}

		return result;
	}

	public Collection<GridValue> pointsInCell(int cell) {
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

	public void set(Entity entity, String entityType) {
		String id = entity.id;
		Vector3 vector3 = entity.transform.vector3;
		GridValue existingValue = objectIndex.get(id);
		if (existingValue != null) {
			Vector3 existingVector = existingValue.entity.transform.vector3;
			if (entity.transform.vector3.x == existingVector.x
					&& entity.transform.vector3.y == existingVector.y) {
				return;
			}
		}

		int cell = hash(vector3.x, vector3.y);
		GridValue gridValue = new GridValue(id, cell, entity, entityType);
		objectIndex.put(id, gridValue);

		if (!cells.containsKey(cell)) {
			cells.put(cell, new ConcurrentHashMap<String, GridValue>());
		}
		cells.get(cell).put(id, gridValue);
	}

	private int hash(float x, float y) {
		return Math.round((x * this.convFactor))
				+ Math.round((y * this.convFactor)) * this.width;
	}
}
