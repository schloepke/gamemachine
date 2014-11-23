package io.gamemachine.pathfinding;

import io.gamemachine.client.messages.GridData;
import io.gamemachine.client.messages.PathData;
import io.gamemachine.client.messages.TriangleMesh;
import io.gamemachine.client.messages.Vector3;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.google.common.io.Files;

public class MeshImporter {

	public static GridGraph importGrid() {
		try {
			byte[] bytes = Files.toByteArray(new File("/home/chris/game_machine/server/grid_data.bin"));
			GridData gridData = GridData.parseFrom(bytes);
			return createGridGraph(gridData);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static GridGraph createGridGraph(GridData gridData) {
		int h = gridData.h;
		int w = gridData.w;
		System.out.println("width=" + w + " height=" + h);
		int index = 0;
		GridGraph graph = new GridGraph(w, h);

		for (Vector3 point : gridData.points) {
			Node node = new Node();
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
			graph.nodes[(int) node.position.x][(int) node.position.y] = node;

			node.index = index;
			graph.nodeIndex.put(node.index, node);
			graph.grid.set(node.index, node.position.x, node.position.y, node.position.z);
			index++;
		}

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Node node = graph.nodes[x][y];
				if (x > 0) {
					Node left = graph.nodes[x - 1][y];
					addIfWalkable(node, left);
				}

				if (x < (w - 1)) {
					Node right = graph.nodes[x + 1][y];
					addIfWalkable(node, right);
				}

				if (y < (h - 1)) {
					Node up = graph.nodes[x][y + 1];
					addIfWalkable(node, up);
				}

				if (y > 0) {
					Node down = graph.nodes[x][y - 1];
					addIfWalkable(node, down);
				}

			}
		}
		return graph;
	}

	public static void addIfWalkable(Node node, Node other) {
		boolean valid = false;
		if ((node.position.z > other.position.z) && (node.position.z - other.position.z) <= 0.4) {
			valid = true;
		} else if ((node.position.z < other.position.z) && (other.position.z - node.position.z) <= 0.4) {
			valid = true;
		}
		
		if (angle(other.position.z, 1d) >= 45) {
			valid = false;
		}
		
		if (valid) {
			node.connections.add(new DefaultConnection<Node>(node, other));
		}
	}

	public static double angle(double z, double targetZ) {
		io.gamemachine.util.Vector3 current = new io.gamemachine.util.Vector3(1d, z, 0d);
		io.gamemachine.util.Vector3 target = new io.gamemachine.util.Vector3(1d, targetZ, 0d);
		double center = 0d;
		return Math.toDegrees(Math.atan2(current.x - center, current.y - center)
				- Math.atan2(target.x - center, target.y - center));
	}

	public static NavmeshGraph importMesh() {
		TriangleMesh mesh = loadMesh();
		if (mesh == null) {
			return null;
		}
		return meshToGraph(mesh);
	}

	public static void stresstest(NavmeshGraph graph, int iterations) {
		for (int i = 1; i < iterations; i++) {
			graph.findPath(10d, 10d, 100d, 100d);
		}
	}

	public static void writePathData(PathData pathData) {
		byte[] bytes = pathData.toByteArray();
		File file = new File("/home/chris/game_machine/server/pathdata.bin");
		try {
			Files.write(bytes, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static TriangleMesh loadMesh() {
		try {
			byte[] bytes = Files.toByteArray(new File("/home/chris/game_machine/server/mesh.bin"));
			return TriangleMesh.parseFrom(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String VectortoString(Vector3 v) {
		return "(" + v.x + ", " + v.y + ", " + v.z + ")";
	}

	public static TriangleMesh flipTriangles(TriangleMesh mesh) {

		Integer[] array = new Integer[mesh.indices.size()];
		mesh.indices.toArray(array);

		for (int i = 0; i < mesh.indices.size() / 3; i++) {
			int t = array[i * 3];
			array[i * 3] = array[(i * 3) + 2];
			array[(i * 3) + 2] = t;
		}
		mesh.indices = Arrays.asList(array);
		return mesh;
	}

	/*
	 * Takes a triangle mesh and turns it into a graph that gdx-ai can work with
	 */
	public static NavmeshGraph meshToGraph(TriangleMesh mesh) {
		mesh = flipTriangles(mesh);
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		NavmeshGraph graph = new NavmeshGraph();
		int index = 0;

		for (Vector3 vec : mesh.vertices) {
			Node node = new Node();
			node.position = new io.gamemachine.util.Vector3(vec.x, vec.y, vec.z);
			if (nodes.containsKey(node.position.toString())) {
				continue;
			}
			node.index = index;
			graph.nodes.put(node.index, node);
			nodes.put(node.position.toString(), node);
			graph.grid.set(node.index, node.position.x, node.position.y, node.position.z);
			index++;
		}

		int triangleCount = mesh.indices.size() / 3;
		Node c1;
		Node c2;
		Node c3;
		for (Node node : nodes.values()) {
			for (int i = 0; i < triangleCount; i++) {
				io.gamemachine.util.Vector3 v1 = fromVector3(mesh.vertices.get(mesh.indices.get(i * 3)));
				io.gamemachine.util.Vector3 v2 = fromVector3(mesh.vertices.get(mesh.indices.get(i * 3 + 1)));
				io.gamemachine.util.Vector3 v3 = fromVector3(mesh.vertices.get(mesh.indices.get(i * 3 + 2)));

				if (node.position.isEqualTo(v1)) {
					c2 = nodes.get(v2.toString());
					c3 = nodes.get(v3.toString());
					node.connections.add(new DefaultConnection<Node>(node, c2));
					node.connections.add(new DefaultConnection<Node>(node, c3));
				} else if (node.position.isEqualTo(v2)) {
					c1 = nodes.get(v1.toString());
					c3 = nodes.get(v3.toString());
					node.connections.add(new DefaultConnection<Node>(node, c1));
					node.connections.add(new DefaultConnection<Node>(node, c3));
				} else if (node.position.isEqualTo(v3)) {
					c1 = nodes.get(v1.toString());
					c2 = nodes.get(v2.toString());
					node.connections.add(new DefaultConnection<Node>(node, c1));
					node.connections.add(new DefaultConnection<Node>(node, c2));
				}
			}
		}
		return graph;
	}

	public static PathData fromPathResult(PathResult result) {
		PathData pathData = new PathData();
		pathData.startPoint = toVector3(result.startNode.position);
		pathData.endPoint = toVector3(result.endNode.position);

		for (int i = 0; i < result.resultPath.getCount(); i++) {
			Node node = (Node) result.resultPath.get(i);
			pathData.addNodes(toVector3(node.position));
		}
		return pathData;
	}

	public static io.gamemachine.util.Vector3 fromVector3(Vector3 v) {
		return new io.gamemachine.util.Vector3(v.x, v.y, v.z);
	}

	public static Vector3 toVector3(io.gamemachine.util.Vector3 v1) {
		Vector3 v2 = new Vector3();
		v2.x = (float) v1.x;
		v2.y = (float) v1.z;
		v2.z = (float) v1.y;
		return v2;
	}

}
