package io.gamemachine.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;


public class GridConfigs {
	
	private static Map<String, GridConfig> gridConfigs = new HashMap<String,GridConfig>();
	
	public static void setGridConfigs(List<? extends Config> values) {
		for (Config value : values) {
			GridConfig gridConfig = new GridConfig(value);
			gridConfigs.put(gridConfig.name,gridConfig);
		}
	}
	
	public static Collection<GridConfig> getGridConfigs() {
		return gridConfigs.values();
	}
	
	public static GridConfig getGridConfig(String name) {
		return gridConfigs.get(name);
	}
}
