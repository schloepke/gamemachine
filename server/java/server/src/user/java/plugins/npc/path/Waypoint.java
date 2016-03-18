package plugins.npc.path;

import io.gamemachine.unity.unity_engine.unity_types.Vector3;

/**
 * Created by chris on 3/1/2016.
 */
public class Waypoint {
    public int order;
    public Vector3 position;

    public Waypoint(Vector3 position, int order) {
        this.position = position;
        this.order = order;
    }
}
