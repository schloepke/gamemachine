package plugins.npc.path;

import com.google.common.collect.Lists;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chris on 3/1/2016.
 */
public class WaypointRoute {

    public enum Type {
        ReverseOnEnd,
        Circular,
        Static
    }


    private List<Waypoint> waypoints = new ArrayList<Waypoint>();
    public int wpIndex = 0;
    private Waypoint currentWaypoint;

    public Type type;
    public boolean showAtRuntime = false;

    public WaypointRoute(List<Waypoint> waypoints) {
        Collections.sort(waypoints, (Waypoint o1, Waypoint o2) ->
                o1.order - o2.order);

        this.waypoints = waypoints;
    }

    public void reset() {
        wpIndex = 0;
        currentWaypoint = waypoints.get(wpIndex);
    }

    public int currentWaypointIndex() {
        return wpIndex;
    }

    public Waypoint currentWaypoint() {
        if (currentWaypoint == null) {
            return nextWaypoint();
        } else {
            return nextWaypoint();
        }
    }

    public Waypoint nextWaypoint() {
        if (type == Type.Static) {
            return currentWaypoint = waypoints.get(wpIndex);
        }

        if (wpIndex >= (waypoints.size() - 1)) {
            if (type == Type.ReverseOnEnd) {
                waypoints = Lists.reverse(waypoints);
                wpIndex = 0;
            } else if (type == Type.Circular) {
                wpIndex = 0;
            }
        } else {
            wpIndex++;
        }

        return currentWaypoint = waypoints.get(wpIndex);
    }
}

