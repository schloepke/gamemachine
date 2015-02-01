package io.gamemachine.pathfinding;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import io.gamemachine.client.messages.PathData;
import io.gamemachine.client.messages.Vector3;
import io.gamemachine.pathfinding.grid.Graph;

public class Util {
	
	public static String VectortoString(Vector3 v) {
		return "(" + v.x + ", " + v.y + ", " + v.z + ")";
	}
	
	public static void stresstest(Graph graph, io.gamemachine.util.Vector3 start, io.gamemachine.util.Vector3 end,
			boolean smoothPath, boolean cover, int iterations) {
		for (int i = 1; i < iterations; i++) {
			graph.findPath(start,end, smoothPath, cover);
		}
	}

	public static void writePathData(PathData pathData) {
		byte[] bytes = pathData.toByteArray();
		File file = new File("/home/chris/game_machine/server/java/shared/pathdata.bin");
		try {
			Files.write(bytes, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PathData fromPathResult(PathResult result) {
		PathData pathData = new PathData();
		pathData.startPoint = toVector3(result.startNode.position);
		pathData.endPoint = toVector3(result.endNode.position);

		if (result.smoothPath != null) {
			System.out.println("Exporting smooth path");
			for (io.gamemachine.util.Vector3 vec : result.smoothPath) {
				pathData.addNodes(toVector3(vec));
			}
		} else {
			for (int i = 0; i < result.resultPath.getCount(); i++) {
				Node node = (Node) result.resultPath.get(i);
				pathData.addNodes(toVector3(node.position));
			}
		}
		return pathData;
	}

	public static io.gamemachine.util.Vector3 fromVector3(Vector3 v) {
		if (v.x == null) {
			v.x = 0f;
		}
		if (v.y == null) {
			v.y = 0f;
		}
		if (v.z == null) {
			v.z = 0f;
		}
		return new io.gamemachine.util.Vector3(v.x, v.y, v.z);
	}

	public static Vector3 toVector3(io.gamemachine.util.Vector3 v1) {
		Vector3 v2 = new Vector3();
		v2.x = (float) v1.x;
		v2.y = (float) v1.z;
		v2.z = (float) v1.y;
		return v2;
	}
}
