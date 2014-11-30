package io.gamemachine.pathfinding.mesh;

import io.gamemachine.client.messages.TriangleMesh;
import io.gamemachine.client.messages.TriangleMesh2;
import io.gamemachine.client.messages.Vector3;
import io.gamemachine.pathfinding.Node;
import io.gamemachine.pathfinding.Util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.critterai.nmgen.ContourSet;
import org.critterai.nmgen.ContourSetBuilder;
import org.critterai.nmgen.DetailMeshBuilder;
import org.critterai.nmgen.OpenHeightfield;
import org.critterai.nmgen.OpenHeightfieldBuilder;
import org.critterai.nmgen.PolyMeshField;
import org.critterai.nmgen.PolyMeshFieldBuilder;
import org.critterai.nmgen.SolidHeightfield;
import org.critterai.nmgen.SolidHeightfieldBuilder;
import org.critterai.nmgen.SpanFlags;

import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.google.common.io.Files;

public class MeshImporter {

	

	public static NavmeshGraph importMesh() {
		TriangleMesh mesh = loadMesh();
		if (mesh == null) {
			return null;
		}
		return meshToGraph(mesh);
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

	public static void BuildNavMesh() {
		TriangleMesh2 input = null;
		try {
			byte[] bytes = Files.toByteArray(new File("/home/chris/game_machine/server/java/shared/mesh.bin"));
			input = TriangleMesh2.parseFrom(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		SolidHeightfieldBuilder sfbuilder = new SolidHeightfieldBuilder(1f, 0.5f, 5, 4, 45f, true);
		float[] vertices = new float[input.vertices.size()];
		for(int i = 0; i < input.vertices.size(); i++) vertices[i] = input.vertices.get(i);
		
		int[] indices = new int[input.indices.size()];
		for(int i = 0; i < input.indices.size(); i++) indices[i] = input.indices.get(i);
		
		SolidHeightfield sf = sfbuilder.build(vertices, indices);
		OpenHeightfieldBuilder hfbuilder = new OpenHeightfieldBuilder(4,5,1,0,SpanFlags.WALKABLE, true, null);
		OpenHeightfield hf = hfbuilder.build(sf, true);
		ContourSetBuilder csbuilder = new ContourSetBuilder(null);
		ContourSet set = csbuilder.build(hf);
		PolyMeshFieldBuilder pbuilder = new PolyMeshFieldBuilder(6);
		PolyMeshField mf = pbuilder.build(set);
		DetailMeshBuilder dtbuilder = new DetailMeshBuilder(2,2);
		org.critterai.nmgen.TriangleMesh tmesh = dtbuilder.build(mf, hf);
		
		System.out.println(tmesh.vertCount());
		TriangleMesh2 out = new TriangleMesh2();
		Float[] verts = new Float[tmesh.vertices.length];
		for(int i = 0; i < tmesh.vertices.length; i++) verts[i] = tmesh.vertices[i];
		
		Integer[] indexes = new Integer[tmesh.indices.length];
		for(int i = 0; i < tmesh.indices.length; i++) indexes[i] = tmesh.indices[i];
		
		out.vertices = Arrays.asList(verts);
		out.indices = Arrays.asList(indexes);
		
		byte[] bytes = out.toByteArray();
		File file = new File("/home/chris/game_machine/server/java/shared/outmesh.bin");
		try {
			Files.write(bytes, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				io.gamemachine.util.Vector3 v1 = Util.fromVector3(mesh.vertices.get(mesh.indices.get(i * 3)));
				io.gamemachine.util.Vector3 v2 = Util.fromVector3(mesh.vertices.get(mesh.indices.get(i * 3 + 1)));
				io.gamemachine.util.Vector3 v3 = Util.fromVector3(mesh.vertices.get(mesh.indices.get(i * 3 + 2)));

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

	

}
