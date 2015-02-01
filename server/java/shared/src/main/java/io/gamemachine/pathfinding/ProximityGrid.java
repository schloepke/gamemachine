package io.gamemachine.pathfinding;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProximityGrid {

	private int max;
	private int cellSize = 0;
	private double convFactor;
	private int width;
	private int cellCount;

	private static final Logger logger = LoggerFactory.getLogger(ProximityGrid.class);

	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GridValue>> gridValues = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GridValue>>();

	private ConcurrentHashMap<Integer, GridValue> objectIndex = new ConcurrentHashMap<Integer, GridValue>();
	private ConcurrentHashMap<Integer, Integer> cellsIndex = new ConcurrentHashMap<Integer, Integer>();
	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GridValue>> cells = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, GridValue>>();

	public ProximityGrid(int max, int cellSize) {
		this.max = max;
		this.cellSize = cellSize;
		this.convFactor = 1.0f / this.cellSize;
		this.width = (int) (this.max / this.cellSize);
		this.cellCount = this.width * this.width;
	}

	public class GridValue {
		public double x;
		public double y;
		public double z;
		public int id;

		public GridValue(int id, double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.id = id;
		}
	}

	public int getObjectCount() {
		return objectIndex.size();
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

	public Set<Integer> cellsWithinBounds(double x, double y) {
		Set<Integer> cells = new HashSet<Integer>();

		int offset = this.cellSize;

		int startX = (int) (x - offset);
		int startY = (int) (y - offset);

		// subtract one from offset to keep it from hashing to the next cell
		// boundary outside of range
		int endX = (int) (x + offset - 1);
		int endY = (int) (y + offset - 1);

		for (int rowNum = startX; rowNum <= endX; rowNum += offset) {
			for (int colNum = startY; colNum <= endY; colNum += offset) {
				cells.add(hash(rowNum, colNum));
				if (rowNum >= 0 && colNum >= 0) {
					
				}
			}
		}
		return cells;
	}

	public ArrayList<GridValue> neighbors(double x, double y) {
		ArrayList<GridValue> result;

		Collection<GridValue> gridValues;
		result = new ArrayList<GridValue>();
		Set<Integer> cells = cellsWithinBounds(x, y);
		for (int cell : cells) {
			gridValues = gridValuesInCell(cell);
			if (gridValues == null) {
				continue;
			}

			for (GridValue gridValue : gridValues) {
				if (gridValue != null) {
					result.add(gridValue);
				}
			}
		}
		return result;
	}

	public Collection<GridValue> gridValuesInCell(int cell) {
		ConcurrentHashMap<Integer, GridValue> cellGridValues = cells.get(cell);

		if (cellGridValues != null) {
			return cellGridValues.values();
		} else {
			return null;
		}
	}

	public List<GridValue> getAll() {
		return new ArrayList<GridValue>(objectIndex.values());
	}

	public GridValue get(String id) {
		return objectIndex.get(id);
	}

	public void remove(String playerId) {
		GridValue indexValue = objectIndex.get(playerId);
		if (indexValue != null) {
			int cell = cellsIndex.get(playerId);
			ConcurrentHashMap<Integer, GridValue> cellGridValues = cells.get(cell);
			if (cellGridValues != null) {
				cellGridValues.remove(playerId);
			}
			objectIndex.remove(playerId);
			cellsIndex.remove(playerId);
			gridValues.remove(playerId);
		}
	}

	public Boolean set(int id, double x, double y, double z) {
		GridValue gridValue = new GridValue(id, x, y, z);
		gridValue.id = id;
		gridValue.x = x;
		gridValue.y = y;
		gridValue.z = z;
		return set(gridValue);
	}

	public Boolean set(GridValue gridValue) {

		Boolean hasExisting = false;
		Integer oldCellValue = -1;
		int id = gridValue.id;

		if (objectIndex.containsKey(id)) {
			hasExisting = true;
			oldCellValue = cellsIndex.get(id);
		}

		int cell = hash(gridValue.x, gridValue.y);

		if (hasExisting) {
			if (oldCellValue != cell) {
				ConcurrentHashMap<Integer, GridValue> cellGridValues = cells.get(oldCellValue);
				cellGridValues.remove(id);
				if (cellGridValues.size() == 0) {
					cells.remove(oldCellValue);
				}

			}
			objectIndex.replace(id, gridValue);
			cellsIndex.replace(id, cell);
		} else {
			cellsIndex.put(id, cell);
			objectIndex.put(id, gridValue);
		}

		if (!cells.containsKey(cell)) {
			cells.put(cell, new ConcurrentHashMap<Integer, GridValue>());
		}
		cells.get(cell).put(id, gridValue);


		return true;
	}

	public int hash2(double x, double y) {
		return (int) (Math.floor(x / this.cellSize) + Math.floor(y / this.cellSize) * width);
	}

	public int hash(double x, double y) {
		return (int) ((x * this.convFactor)) + (int) ((y * this.convFactor)) * this.width;
	}
}
