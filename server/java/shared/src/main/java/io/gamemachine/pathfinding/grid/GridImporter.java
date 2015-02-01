package io.gamemachine.pathfinding.grid;

import io.gamemachine.client.messages.GridData;
import io.gamemachine.client.messages.GridVerticle;
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
		Graph graph = new Graph(w, h);

		for (GridVerticle gridVerticle : gridData.gridVerticles) {
			Verticle verticle = Verticle.fromGridVerticle(graph,gridVerticle);
			graph.verticles[verticle.x][verticle.y] = verticle;
			for (Node node : verticle.nodes) {
				graph.nodeMap.put(node.index, node);
			}
		}

		System.out.println("Nodecount=" + graph.nodeIndex);
		int connectionCount = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Verticle verticle = graph.verticles[x][y];
				if (verticle == null) {
					continue;
				}
				if (verticle.nodes == null) {
					continue;
				}
				for (Node node : verticle.nodes) {
					for (Node neighbor : neighbors(graph.verticles, node, x, y)) {
						if (addConnection(node, neighbor)) {
							connectionCount++;
						}
					}
				}
			}
		}
		System.out.println("connection count " + connectionCount);
		return graph;
	}

	private boolean inBounds(Verticle[][] verticles, int a, int b) {
		try {
			if (a == -1 || a == verticles.length || b == -1 || b == verticles[a].length) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			System.out.println("exception out of bounds " + a + "." + b);
			throw new RuntimeException("Out of bounds");
		}
	}

	private void addNeighbor(Verticle verticle, Node node, List<Node> neighbors) {
		if (verticle == null) {
			return;
		}
		Node neighbor = verticle.getNearest(node.position.z);
		if (neighbor != null) {
			neighbors.add(neighbor);
		}
	}
	
	private List<Node> neighbors(Verticle[][] verticles, Node node, int x, int y) {
		List<Node> neighbors = new ArrayList<Node>();
		int a, b;

		a = x - 1;
		b = y;
		if (inBounds(verticles, a, b)) {
			Verticle verticle = verticles[a][b];
			addNeighbor(verticle,node,neighbors);
		}

		a = x + 1;
		b = y;
		if (inBounds(verticles, a, b)) {
			Verticle verticle = verticles[a][b];
			addNeighbor(verticle,node,neighbors);
		}

		a = x;
		b = y + 1;
		if (inBounds(verticles, a, b)) {
			Verticle verticle = verticles[a][b];
			addNeighbor(verticle,node,neighbors);
		}

		a = x;
		b = y - 1;
		if (inBounds(verticles, a, b)) {
			Verticle verticle = verticles[a][b];
			addNeighbor(verticle,node,neighbors);
		}

		if (Graph.useDiagonals) {
			a = x + 1;
			b = y + 1;
			if (inBounds(verticles, a, b)) {
				Verticle verticle = verticles[a][b];
				addNeighbor(verticle,node,neighbors);
			}

			a = x - 1;
			b = y + 1;
			if (inBounds(verticles, a, b)) {
				Verticle verticle = verticles[a][b];
				addNeighbor(verticle,node,neighbors);
			}

			a = x + 1;
			b = y - 1;
			if (inBounds(verticles, a, b)) {
				Verticle verticle = verticles[a][b];
				addNeighbor(verticle,node,neighbors);
			}

			a = x - 1;
			b = y - 1;
			if (inBounds(verticles, a, b)) {
				Verticle verticle = verticles[a][b];
				addNeighbor(verticle,node,neighbors);
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
		
		if (other.slope > Graph.maxSlope) {
			valid = false;
		}
		
		if (valid) {
			double cost = Graph.baseConnectionCost;

			for (int i = 0; i < Graph.slopePenalties.length; i += 2) {
				double max = Graph.slopePenalties[i];
				double penalty = Graph.slopePenalties[i + 1];

				if (other.slope > max) {
					cost += penalty;
				}
			}

			for (int i = 0; i < Graph.heightPenalties.length; i += 2) {
				double max = Graph.heightPenalties[i];
				double penalty = Graph.heightPenalties[i + 1];

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
