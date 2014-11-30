package io.gamemachine.pathfinding.grid;

import io.gamemachine.client.messages.GridData;
import io.gamemachine.client.messages.GridNode;
import io.gamemachine.client.messages.Vector3;
import io.gamemachine.pathfinding.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

public class GridImporter {
	
	
	public Graph importFromFile(File path) {
		try {
			byte[] bytes = Files.toByteArray(path);
			GridData gridData = GridData.parseFrom(bytes);
			return createGraph(gridData);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Graph createGraph(GridData gridData) {
		int h = gridData.h;
		int w = gridData.w;
		System.out.println("width=" + w + " height=" + h);
		int index = 0;
		Graph graph = new Graph(w, h);

		for (GridNode gridNode : gridData.nodes) {
			Vector3 point = gridNode.point;
			Node node = new Node();
			if (gridNode.slope == null) {
				gridNode.slope = 0f;
			}

			node.slope = gridNode.slope;

			if (point.x == null) {
				point.x = 0f;
			}
			if (point.y == null) {
				point.y = 0f;
			}
			if (point.z == null) {
				point.z = 0f;
			}

			// System.out.println(point.x+" "+point.y+" "+point.z);
			node.position = new io.gamemachine.util.Vector3(point.x, point.z, point.y);

			if (node.slope > Graph.maxSlope) {
				graph.nodes[(int) node.position.x][(int) node.position.y] = null;
				continue;
			}

			graph.nodes[(int) node.position.x][(int) node.position.y] = node;

			node.index = index;
			graph.nodeIndex.put(node.index, node);
			graph.grid.set(node.index, node.position.x, node.position.y, node.position.z);
			index++;
		}

		System.out.println("Nodecount=" + index);
		int connectionCount = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Node node = graph.nodes[x][y];
				for (Node neighbor : neighbors(graph.nodes, x, y)) {
					if (addConnection(node, neighbor)) {
						connectionCount++;
					}
				}
			}
		}
		System.out.println("connection count " + connectionCount);
		System.out.println("nodeIndex size " + graph.nodeIndex.size());
		return graph;
	}

	private boolean inBounds(Node[][] nodes, int a, int b) {
		try {
			if (a == -1 || a == nodes.length || b == -1 || b == nodes[a].length) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			System.out.println("exception out of bounds " + a + "." + b);
			throw new RuntimeException("Out of bounds");
		}
	}

	private List<Node> neighbors(Node[][] nodes, int x, int y) {
		List<Node> neighbors = new ArrayList<Node>();

		int a, b;

		a = x - 1;
		b = y;
		if (inBounds(nodes, a, b)) {
			Node left = nodes[a][b];
			neighbors.add(left);
		}

		a = x + 1;
		b = y;
		if (inBounds(nodes, a, b)) {
			Node right = nodes[a][b];
			neighbors.add(right);
		}

		a = x;
		b = y + 1;
		if (inBounds(nodes, a, b)) {
			Node up = nodes[a][b];
			neighbors.add(up);
		}

		a = x;
		b = y - 1;
		if (inBounds(nodes, a, b)) {
			Node down = nodes[a][b];
			neighbors.add(down);
		}

		if (Graph.useDiagonals) {
			a = x + 1;
			b = y + 1;
			if (inBounds(nodes, a, b)) {
				Node topright = nodes[a][b];
				neighbors.add(topright);
			}

			a = x - 1;
			b = y + 1;
			if (inBounds(nodes, a, b)) {
				Node topleft = nodes[a][b];
				neighbors.add(topleft);
			}

			a = x + 1;
			b = y - 1;
			if (inBounds(nodes, a, b)) {
				Node botright = nodes[a][b];
				neighbors.add(botright);
			}

			a = x - 1;
			b = y - 1;
			if (inBounds(nodes, a, b)) {
				Node botleft = nodes[a][b];
				neighbors.add(botleft);
			}
		}
		return neighbors;
	}

	private boolean addConnection(Node node, Node other) {
		boolean valid = false;
		double slopeDiff = 0.0f;
		if (node == null || other == null) {
			return false;
		}
		
		double stepCost = node.stepCost(other);
		double slopeCost = node.slopeCost(other);
		if (stepCost <= Graph.maxStep) {
			valid = true;
		}
		

		if (valid) {
			double cost = Graph.baseConnectionCost;
			
			for (int i=0; i<Graph.slopePenalties.length;i+=2) {
				double max = Graph.slopePenalties[i];
				double penalty = Graph.slopePenalties[i+1];
				
				if (other.slope > max) {
					cost += penalty;
				}
			}
			
			for (int i=0; i<Graph.heightPenalties.length;i+=2) {
				double max = Graph.heightPenalties[i];
				double penalty = Graph.heightPenalties[i+1];
				
				if (other.position.z > max) {
					cost += penalty;
				}
			}
			
			GridConnection<Node> con = new GridConnection<Node>(node, other);
			con.cost = (float) cost;
			node.connections.add(con);
		}
		return valid;
	}
}
