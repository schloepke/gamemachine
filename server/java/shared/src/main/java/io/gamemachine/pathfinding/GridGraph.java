package io.gamemachine.pathfinding;

import io.gamemachine.pathfinding.ProximityGrid.GridValue;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class GridGraph implements IndexedGraph<Node> {

public ProximityGrid grid;
	
	public Node[][] nodes;
	public HashMap<Integer,Node> nodeIndex = new HashMap<Integer,Node>();
	public int height;
	public int width;
	
	public GridGraph(int width, int height) {
		this.width = width;
		this.height = height;
		nodes = new Node[width][height];
		grid = new ProximityGrid(2000,10);
	}
	
	@Override
	public int getNodeCount() {
		return nodeIndex.values().size();
	}

	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		return nodeIndex.get(fromNode.index).getConnections();
	}
	
	public Node findClosestNode(double x, double y) {
		int xi = (int) Math.round(x);
		int yi = (int) Math.round(y);
		return nodes[xi][yi];
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
		
		
		ManhattanGrid he = new ManhattanGrid();
		IndexedAStarPathFinder<Node> finder = new IndexedAStarPathFinder<Node>(this, true);
		pathResult.resultPath = new DefaultGraphPath<Node>();
		pathResult.result = finder.searchNodePath(pathResult.startNode, pathResult.endNode, he, pathResult.resultPath);
		return pathResult;
	}

}
