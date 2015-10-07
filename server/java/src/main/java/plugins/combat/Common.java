package plugins.combat;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.TrackData;
import scala.Console;
import io.gamemachine.messages.GmVector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Common {

	public static Long npcdDeathTime = 180000L;
	public static Long deathTime = 15000L;
	public static int worldOffset = 1000;
	public static String gameId = "mygame";
	public static double vitalsDistance = 50;
	
	public static Grid playerGrid(String playerId) {
		return GameGrid.getGameGrid("default", playerId);
	}
	
	public static Grid objectGrid(String playerId) {
		return GameGrid.getGameGrid("build_objects", playerId);
	}
	
	public static List<TrackData> getTargetsInRange(int range, int x, int y, int z, String gridName, String playerId) {
		GmVector3 vec = new GmVector3();
		vec.xi = x;
		vec.yi = y;
		vec.zi = z;
		return getTargetsInRange(range,vec,gridName, playerId);
	}
	
	public static List<TrackData> getTargetsInRange(int range, int x, int y, int z, Grid grid) {
		GmVector3 vec = new GmVector3();
		vec.xi = x;
		vec.yi = y;
		vec.zi = z;
		return getTargetsInRange(range,vec,grid);
	}
	
	public static List<TrackData> getTargetsInRange(int range, GmVector3 location, Grid grid) {
		List<TrackData> targets = new ArrayList<TrackData>();
		for (TrackData trackData : grid.getAll()) {
			
			// Lets you choose a specific radius, as the spatial query is not exact.
			if (range > 0) {
				double distance = distance(location.xi,location.yi,location.zi,trackData);
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
		Grid grid = GameGrid.getGameGrid(gameId, gridName, playerId);
		return getTargetsInRange(range,location,grid);
	}

	public static double scale(int i) {
		return (i / 100l) - worldOffset;
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
		//Console.out().println(x1+"."+y1+"."+z1+" - "+x2+"."+y2+"."+z2);
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}
	
	public static io.gamemachine.util.Vector3 toVector3(GmVector3 from) {
		io.gamemachine.util.Vector3 to = new io.gamemachine.util.Vector3();
		to.x = scale(from.xi);
		to.y = scale(from.yi);
		to.z = scale(from.zi);
		return to;
	}
	
	public static io.gamemachine.util.Vector3 toVector3(int x, int y) {
		io.gamemachine.util.Vector3 to = new io.gamemachine.util.Vector3();
		to.x = scale(x);
		to.y = scale(y);
		to.z = 0l;
		return to;
	}
	
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
