package io.gamemachine.config;

import com.typesafe.config.Config;

public class GridConfig {

    public enum GridType {
        None,
        Moving,
        Static
    }

    public final String name;
    public final int gridSize;
    public final int cellSize;
    public final GridType type;

    public GridConfig(Config config) {
        this.name = config.getString("name");
        this.gridSize = config.getInt("grid_size");
        this.cellSize = config.getInt("cell_size");
        String type = config.getString("type");
        if (type.equals("static")) {
            this.type = GridType.Static;
        } else if (type.equals("moving")) {
            this.type = GridType.Moving;
        } else {
            this.type = GridType.None;
        }
    }

}