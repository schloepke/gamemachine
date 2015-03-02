package io.gamemachine.game_systems;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.pathfinding.Util;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PathData;
import io.gamemachine.messages.Vector3;
import io.gamemachine.pathfinding.Node;
import io.gamemachine.pathfinding.PathResult;
import io.gamemachine.pathfinding.grid.Graph;
import io.gamemachine.pathfinding.grid.GridImporter;

import java.io.File;

public class PathService extends GameMessageActor {

	public static String name = "PathService";
	private Graph graph;

	@Override
	public void awake() {
		File path = new File("/home/chris/game_machine/server/java/shared/grid_data.bin");
		GridImporter graphImport = new GridImporter();
		graph = graphImport.importFromFile(path);
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		PathData pathData = gameMessage.pathData;
		io.gamemachine.util.Vector3 start = fromVector3(pathData.startPoint);
		io.gamemachine.util.Vector3 end = fromVector3(pathData.endPoint);
		System.out.println("Start="+start.toString()+" end="+end.toString());
		PathResult result = graph.findPath(start, end, false, true);
		pathData = fromPathResult(result);
		if (pathData != null) {
			GameMessage m = new GameMessage();
			m.pathData = pathData;
			System.out.println("pathdata.nodes "+m.pathData.nodes.size());
			PlayerCommands.sendGameMessage(m, playerId);
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {

	}

	public static PathData fromPathResult(PathResult result) {
		if (!result.result) {
			System.out.println(result.error);
			return null;
		}
		System.out.println("Pathcount "+result.resultPath.getCount());
		

		PathData pathData = new PathData();
		pathData.startPoint = toVector3(result.startNode.position);
		pathData.endPoint = toVector3(result.endNode.position);

		if (result.smoothPath != null) {
			System.out.println("smooth Pathcount "+result.smoothPath.size());
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

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub
		
	}

}
