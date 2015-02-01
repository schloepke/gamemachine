package io.gamemachine.pathfinding.grid;

import io.gamemachine.client.messages.GridNode;
import io.gamemachine.client.messages.GridVerticle;
import io.gamemachine.pathfinding.Node;

import java.util.ArrayList;
import java.util.List;

public class Verticle {

	public int x;
	public int y;
	public int nodeCount = 0;
	public Node primaryNode;
	public List<Node> nodes = new ArrayList<Node>();

	private static int ipart(double x) {
		return (int) Math.floor(x);
	}

	public static Verticle fromGridVerticle(Graph graph, GridVerticle gridVerticle) {
		Verticle verticle = new Verticle();
		if (gridVerticle.x == null) {
			gridVerticle.x = 0f;
		}
		if (gridVerticle.y == null) {
			gridVerticle.y = 0f;
		}

		verticle.x = ipart(gridVerticle.x);
		verticle.y = ipart(gridVerticle.y);
		if (gridVerticle.gridNodes == null) {
			return verticle;
		}

		for (GridNode gridNode : gridVerticle.gridNodes) {
			if (gridNode.slope == null) {
				gridNode.slope = 0f;
			}
			if (gridNode.slope > Graph.maxSlope) {
				continue;
			}

			if (gridNode.point.x == null) {
				gridNode.point.x = 0f;
			}
			if (gridNode.point.y == null) {
				gridNode.point.y = 0f;
			}
			if (gridNode.point.z == null) {
				gridNode.point.z = 0f;
			}

			Node node = new Node();
			node.position = new io.gamemachine.util.Vector3(gridNode.point.x, gridNode.point.y, gridNode.point.z);
			node.slope = gridNode.slope;
			node.index = graph.nodeIndex;
			verticle.add(node);
			graph.nodeIndex++;
		}
		verticle.setPrimaryNode();
		return verticle;
	}

	public void setPrimaryNode() {
		if (nodeCount == 1) {
			this.primaryNode = nodes.get(0);
		}
	}

	public void add(Node node) {
		if (ipart(node.position.x) != this.x || ipart(node.position.y) != this.y) {
			throw new RuntimeException("Node is invalid for verticle " + this.x + "(" + node.position.x + ") " + this.y
					+ "(" + node.position.y + ")");
		}
		for (Node n : nodes) {
			if (n == node) {
				throw new RuntimeException("Duplicate node");
			}
		}
		nodes.add(node);
		this.nodeCount++;
	}

	public Node getNearest(double height) {
		if (nodeCount == 1) {
			double d = Math.abs(height - primaryNode.position.z);
			if (d <= Graph.maxStep) {
				return primaryNode;
			} else {
				return null;
			}
		}

		double bestDistance = 1000f;
		Node best = null;

		for (Node node : nodes) {
			double d = Math.abs(height - node.position.z);
			if (d < bestDistance) {
				best = node;
				bestDistance = d;
			}
		}

		if (bestDistance <= Graph.maxStep) {
			return best;
		} else {
			return null;
		}
	}
}
