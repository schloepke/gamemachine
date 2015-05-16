package plugins.pvp_game.pathfinding;

import io.gamemachine.util.Vector3;

import java.util.List;

import plugins.pvp_game.pathfinding.GridPathfinder.Metrics;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;

public class PathResult {

	public boolean result = false;
	public Node startNode;
	public Node endNode;
	public DefaultGraphPath<Node> resultPath;
	public List<Vector3> smoothPath;
	public int smoothPathCount = 0;
	public String error = "";
	public Metrics metrics;
	
}
