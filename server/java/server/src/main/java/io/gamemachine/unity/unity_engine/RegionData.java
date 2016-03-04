package io.gamemachine.unity.unity_engine;

import io.gamemachine.net.Connection;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/29/2016.
 */
public class RegionData {

    public enum Status {
        Active,
        Inactive
    }

    public String playerId;
    public String region;
    public long lastUpdate;
    public Vector3 position;
    public float size;
    public Status status;
    public boolean isAlive = false;

}
