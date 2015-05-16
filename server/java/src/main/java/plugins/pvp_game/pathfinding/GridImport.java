package plugins.pvp_game.pathfinding;

import io.gamemachine.messages.GridData;
import io.gamemachine.messages.GridNode;
import io.gamemachine.pathfinding.grid.GridConnection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

public class GridImport {

	public Graph graph;
	
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
		int h = 10000;
		int w = 10000;
		graph = new Graph(w, h);
		
		for (GridNode gridNode : gridData.nodes) {
			if (gridNode.slope == null) {
				gridNode.slope = 0f;
			}
			if (gridNode.slope > Graph.maxSlope) {
				continue;
			}

			if (gridNode.point.xi == null) {
				gridNode.point.xi = 0;
			}
			if (gridNode.point.yi == null) {
				gridNode.point.yi = 0;
			}
			if (gridNode.point.zi == null) {
				gridNode.point.zi = 0;
			}
			

			Node node = new Node();
			node.x = gridNode.point.xi;
			node.y = gridNode.point.yi;
			node.z = gridNode.point.zi;
			//node.position = new io.gamemachine.util.Vector3(node.x, node.y, node.z);
			node.slope = gridNode.slope;
			node.index = graph.nodeIndex;
			graph.nodes.put(node.x,node.y,node);
			graph.nodeMap.put(node.index, node);
			graph.nodeIndex++;
		}

		for (Node node : graph.nodes.values()) {
			for (Node other : neighbors(node)) {
				if (other != null) {
					GridConnection<Node> con = new GridConnection<Node>(node, other);
					node.connections.add(con);
				}
			}
		}
		gridData = null;
		return graph;
	}
	
	private List<Node> neighbors(Node node) {
		int step = 2;
		List<Node> neighbors = new ArrayList<Node>();
		int x = node.x;
		int y = node.y;
		int a, b;
		Node other;
		
//		for (int j = x - step; j < x+step; j++) {
//			for (int k = y -step; k < y+step; k++) {
//				if (j == x && k == y) {
//					continue;
//				}
//				other = graph.nodes.get(j, k);
//				if (other != null) {
//					neighbors.add(other);
//				} else {
//					//System.out.println(j+" "+k);
//				}
//				
//			}
//		}
		
		a = x - step;
		b = y;
		other = graph.nodes.get(a, b);
		neighbors.add(other);
		

		a = x + step;
		b = y;
		other = graph.nodes.get(a, b);
		neighbors.add(other);

		a = x;
		b = y + step;
		other = graph.nodes.get(a, b);
		neighbors.add(other);

		a = x;
		b = y - step;
		other = graph.nodes.get(a, b);
		neighbors.add(other);

		if (Graph.useDiagonals) {
			a = x + step;
			b = y + step;
			other = graph.nodes.get(a, b);
			neighbors.add(other);

			a = x - step;
			b = y + step;
			other = graph.nodes.get(a, b);
			neighbors.add(other);

			a = x + step;
			b = y - step;
			other = graph.nodes.get(a, b);
			neighbors.add(other);

			a = x - step;
			b = y - step;
			other = graph.nodes.get(a, b);
			neighbors.add(other);
		}
		return neighbors;
	}
}
