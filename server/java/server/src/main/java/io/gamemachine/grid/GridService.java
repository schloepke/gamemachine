package io.gamemachine.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.typesafe.config.Config;

import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GridConfig;
import io.gamemachine.config.GridConfigs;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;

public class GridService {

	private static final Logger logger = LoggerFactory.getLogger(GridService.class);

	private static ConcurrentHashMap<Integer, ConcurrentHashMap<String, Grid>> gameGrids = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, Grid>>();

	private String defaultGrid;
	
	private static class LazyHolder {
		private static final GridService INSTANCE = new GridService();
	}

	public static GridService getInstance() {
		return LazyHolder.INSTANCE;
	}

	private GridService() {
		Config config = AppConfig.getConfig();
		defaultGrid = config.getString("gamemachine.default_grid");
	}
	
	public void removeEntityFromGrids(String entityId) {
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grid.remove(entityId);
			}
		}
	}
	
	public void removeExpired() {
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grid.RemoveExpired(AppConfig.getGridExpiration());
			}
		}
	}
	
	public List<Grid> getGrids() {
		List<Grid> grids = new ArrayList<Grid>();
		for (int zone : gameGrids.keySet()) {
			for (Grid grid : gameGrids.get(zone).values()) {
				grids.add(grid);
			}
		}
		return grids;
	}
	
	

	public synchronized void createDefaultGrids() {
		for (Zone zone : ZoneService.staticZones()) {
			createForZone(zone.number);
		}
		logger.warn("Created grids for "+ZoneService.staticZones().size()+" zones");
	}
	
	public synchronized void createForZone(int zone) {
		gameGrids.put(zone, new ConcurrentHashMap<String,Grid>());
		Map<String, Grid> grids = gameGrids.get(zone);
		for (GridConfig gridConfig : GridConfigs.getGridConfigs()) {
			Grid grid = Grid.createFromConfig(gridConfig, zone);
			grids.put(gridConfig.name, grid);
		}
	}
	
	public void removeForZone(int zone) {
		if (gameGrids.containsKey(zone)) {
			gameGrids.remove(zone);
		}
	}
	
	public Grid getGrid(int zone, String name) {
		if (Strings.isNullOrEmpty(name)) {
			name = defaultGrid;
		}
		
		if (gameGrids.containsKey(zone)) {
			Map<String, Grid> grids = gameGrids.get(zone);
			if (grids.containsKey(name)) {
				return grids.get(name);
			}
		}
		logger.warn("Grid not found for zone "+zone+" name "+name);
		return null;
	}

	public Grid getPlayerGrid(String name, String playerId) {
		Zone zone =  PlayerService.getInstance().getZone(playerId);
		return getGrid(zone.number,name);
	}
	
}
