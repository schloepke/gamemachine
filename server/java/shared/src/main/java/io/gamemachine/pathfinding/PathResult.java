package io.gamemachine.pathfinding;

import io.gamemachine.pathfinding.GridPathfinder.Metrics;
import io.gamemachine.util.Vector3;

import java.util.List;

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
