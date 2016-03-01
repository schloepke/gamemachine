package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;

import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.TrackData;
import io.gamemachine.unity.unity_engine.Vector3;

public class AoeUtil {

    public static List<TrackData> getTargetsInRange(int range, int x, int y, int z, String gridName, String playerId) {
        GmVector3 vec = new GmVector3();
        vec.xi = x;
        vec.yi = y;
        vec.zi = z;
        return getTargetsInRange(range, vec, gridName, playerId);
    }

    public static List<TrackData> getTargetsInRange(int range, int x, int y, int z, Grid grid) {
        GmVector3 vec = new GmVector3();
        vec.xi = x;
        vec.yi = y;
        vec.zi = z;
        return getTargetsInRange(range, vec, grid);
    }

    public static List<TrackData> getTargetsInRange(int range, GmVector3 location, Grid grid) {
        List<TrackData> targets = new ArrayList<TrackData>();
        for (TrackData trackData : grid.getAll()) {

            // Lets you choose a specific radius, as the spatial query is not exact.
            if (range > 0) {
                //double distance = distance(location.xi,location.yi,location.zi,trackData);
                double distance = distance2d(location.xi, location.zi, trackData);
                if (distance <= range) {
                    targets.add(trackData);
                }
            } else {
                targets.add(trackData);
            }
        }
        return targets;
    }

    public static List<TrackData> getTargetsInRange(int range, GmVector3 location, String gridName, String playerId) {
        Grid grid = GridService.getInstance().getPlayerGrid(gridName, playerId);
        return getTargetsInRange(range, location, grid);
    }

    public static double scale(int i) {
        return (i / 100l) - AppConfig.getWorldOffset();
    }

    public static double distance(TrackData tdata2, TrackData tdata) {
        double x2 = scale(tdata.x);
        double y2 = scale(tdata.y);
        double z2 = scale(tdata.z);
        double x1 = scale(tdata2.x);
        double y1 = scale(tdata2.y);
        double z1 = scale(tdata2.z);
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
    }

    public static double distance(int x, int y, int z, TrackData tdata) {
        double x2 = scale(tdata.x);
        double y2 = scale(tdata.y);
        double z2 = scale(tdata.z);
        double x1 = scale(x);
        double y1 = scale(y);
        double z1 = scale(z);
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
    }

    public static double distance2d(int x, int z, TrackData tdata) {
        double x2 = scale(tdata.x);
        double z2 = scale(tdata.z);
        double x1 = scale(x);
        double z1 = scale(z);
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));
    }

    public static Vector3 toVector3(GmVector3 from) {
        Vector3 to = new Vector3();
        to.x = scale(from.xi);
        to.y = scale(from.yi);
        to.z = scale(from.zi);
        return to;
    }

    public static Vector3 toVector3(int x, int z) {
        Vector3 to = new Vector3();
        to.x = scale(x);
        to.y = 0l;
        to.z = scale(z);
        return to;
    }

}
