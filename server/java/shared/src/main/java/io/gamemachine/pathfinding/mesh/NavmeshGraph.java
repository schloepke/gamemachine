package io.gamemachine.pathfinding.mesh;

import io.gamemachine.pathfinding.Node;
import io.gamemachine.pathfinding.PathResult;
import io.gamemachine.pathfinding.ProximityGrid;
import io.gamemachine.pathfinding.ProximityGrid.GridValue;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

/*
 * Fairly simple for now.  No path smoothing or dynamic obstacles.
 */
public class NavmeshGraph  implements IndexedGraph<Node>{

	public ProximityGrid grid;
	
	public HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
	
	public NavmeshGraph() {
		grid = new ProximityGrid(2000,10);
	}
	
	@Override
	public int getNodeCount() {
		return nodes.values().size();
	}

	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		return nodes.get(fromNode.index).getConnections();
	}
	
	/*
	 * We use the proximity grid to find the closest start/end nodes (triangle vertex).
	 */
	public Node findClosestNode(double x, double y) {
		double best = 500.0;
		GridValue bestGridValue = null;
		ArrayList<GridValue> neighbors = grid.neighbors(x, y);
		for (GridValue gridValue : neighbors) {
			double distance = distance(gridValue.x, gridValue.y, x, y);
			if (distance < best) {
				bestGridValue = gridValue;
				best = distance;
			}
		}
		return nodes.get(bestGridValue.id);
	}
	
	public double distance(double x, double y, double otherX, double otherY) {
		return Math.sqrt(Math.pow(x - otherX, 2) + Math.pow(y - otherY, 2));
	}

	public PathResult findPath(double startX, double startY, double endX, double endY) {
		PathResult pathResult = new PathResult();
		pathResult.startNode = findClosestNode(startX,startY);
		pathResult.endNode = findClosestNode(endX,endY);
		
		if (pathResult.startNode == null) {
			return pathResult;
		}
		
		if (pathResult.endNode == null) {
			return pathResult;
		}
		
		
		ManhattanDistance he = new ManhattanDistance();
		IndexedAStarPathFinder<Node> finder = new IndexedAStarPathFinder<Node>(this, true);
		pathResult.resultPath = new DefaultGraphPath<Node>();
		pathResult.result = finder.searchNodePath(pathResult.startNode, pathResult.endNode, he, pathResult.resultPath);
		return pathResult;
	}
	

}
