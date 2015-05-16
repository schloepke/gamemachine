package plugins.pvp_game.pathfinding;

import io.gamemachine.util.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Graph implements IndexedGraph<Node> {

	public static Graph graph;
	public static boolean useDiagonals = true;
	public static double maxSlope = 45.0;
	public static double maxSmoothSlope = 35d;
	public static double maxStep = 0.4;
	public static double baseConnectionCost = 1f;
	
	public static Double[] slopePenalties = new Double[] {35d,2d,25d,1d};
	public static Double[] heightPenalties = new Double[] {10d,1d};
		
	public static enum Heuristic {MANHATTAN, MANHATTAN2D, EUCLIDEAN};
	public static Heuristic heuristic = Heuristic.MANHATTAN2D;


	public Table<Integer, Integer, Node> nodes;
	
	public HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
	public int height;
	public int width;
	public int nodeIndex = 0;

	public Graph(int width, int height) {
		this.width = width;
		this.height = height;
		nodes = HashBasedTable.create();
	}

	public Node getNode(int x, int y) {
		Node node = nodes.get(x, y);
		if (node == null) {
			return null;
		}
		return node;
	}
	
	@Override
	public int getNodeCount() {
		return nodeMap.values().size();
	}

	@Override
	public Array<Connection<Node>> getConnections(Node fromNode) {
		return nodeMap.get(fromNode.index).getConnections();
	}

	public Node findClosestNode(float x, float y) {
		Vector3 start = new Vector3();
		start.x = x;
		start.y = y;
		return findClosestNode(start);
	}
	
	public Node findClosestNode(Vector3 v) {
		Node closest = null;
		int step = 4;
		int x = (int) Math.round(v.x);
		int y = (int) Math.round(v.y);
		
		closest = getNode(x, y);
		if (closest != null) {
			return closest;
		}
		
		for (int j = x - step; j < x+step; j++) {
			for (int k = y -step; k < y+step; k++) {
				closest = getNode(j, k);
				if (closest != null) {
					return closest;
				}
			}
		}
		return closest;
	}

	public double distance(double x, double y, double otherX, double otherY) {
		return Math.sqrt(Math.pow(x - otherX, 2) + Math.pow(y - otherY, 2));
	}

	public PathResult findPath(float startX, float  startY, float endX, float endY) {
		Vector3 start = new Vector3();
		start.x = startX;
		start.y = startY;
		
		Vector3 end = new Vector3();
		end.x = endX;
		end.y = endY;
		return findPath(start,end,false);
	}
	
	@SuppressWarnings("unchecked")
	public PathResult findPath(Vector3 start, Vector3 end, boolean useFunnel) {
		PathResult pathResult = new PathResult();
		pathResult.startNode = findClosestNode(start);
		pathResult.endNode = findClosestNode(end);

		if (pathResult.startNode == null) {
			pathResult.error = "Start node not found";
			return pathResult;
		}
		
		if (pathResult.startNode.connections.size == 0) {
			pathResult.error = "Node has no connections";
			return pathResult;
		}

		if (pathResult.endNode == null) {
			pathResult.error = "End node not found";
			return pathResult;
		}
		
		if (pathResult.endNode.connections.size == 0) {
			pathResult.error = "Node has no connections";
			return pathResult;
		}

		GridHeuristic he = new GridHeuristic();
		GridPathfinder<Node> finder = new GridPathfinder<Node>(this, true);
		pathResult.metrics = finder.metrics;
		pathResult.resultPath = new DefaultGraphPath<Node>();
		
		pathResult.result = finder.searchNodePath(pathResult.startNode, pathResult.endNode, he, pathResult.resultPath);
		if (pathResult.result) {
			if (useFunnel) {
				
					List<Vector3> vnodes = new ArrayList<Vector3>();
					for (Object obj : pathResult.resultPath.nodes) {
						Node node = (Node) obj;
						vnodes.add(new Vector3(node.x,node.y,0d));
					}
					pathResult.smoothPath = smoothPath(vnodes);
					pathResult.smoothPath = smoothPath2(pathResult.smoothPath, 10);
					pathResult.smoothPath = smoothPath(pathResult.smoothPath);
				
				pathResult.smoothPathCount = pathResult.smoothPath.size();
			}
			
		}

		return pathResult;
	}

	private List<Vector3> smoothPath(List<Vector3> points) {
		if (points.size() <= 1) {
			return points;
		}
		
		List<Vector3> smooth = new ArrayList<Vector3>();
		
		Vector3 start = points.get(0);
		smooth.add(start);
		Vector3 previous = start;
		Vector3 current = null;

		for (Vector3 point : points) {
			current = point;
			if (start != current) {
				int walkable = SuperCover.cover(this, start, current, 1d, 1d);
				if (walkable != 0) {
					smooth.add(previous);
					start = previous;
				}
				previous = current;
			}
		}
		smooth.add(current);
		return smooth;
	}

	private List<Vector3> smoothPath2(List<Vector3> points, int look) {
		if (points.size() <= 1) {
			return points;
		}
		
		List<Vector3> smooth = new ArrayList<Vector3>();
		smooth.add(points.get(0));

		int pathcount = points.size();
		int idx = 0;
		int lastWalkable = 0;
		
		for (int a = 0; a < points.size(); a++) {
			Vector3 start = points.get(idx);
			for (int i = 1; i < look; i++) {
				int currentIdx = idx + i;
				if (currentIdx >= pathcount) {
					break;
				}
				Vector3 end = points.get(currentIdx);
				int walkable = SuperCover.cover(this, start, end, 1d, 1d);
				if (walkable == 0) {
					lastWalkable = currentIdx;
				} else {
					lastWalkable = idx + 1;
					//System.out.println("Not walkable "+walkable+ " : "+start.toString()+" --> "+end.toString());
				}
			}
			
			Vector3 point = points.get(lastWalkable);
			smooth.add(point);
			
			if (point == points.get(pathcount - 1)) {
				break;
			} else {
				idx = lastWalkable;
			}
		}
		return smooth;
	}
	

}
