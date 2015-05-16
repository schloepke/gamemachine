package plugins.pvp_game.pathfinding;

import io.gamemachine.util.Vector3;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class Node implements IndexedNode<Node>{

	public int index;
	public int x;
	public int y;
	public int z;
	public Vector3 position;
	public double slope;
	public Array<Connection<Node>> connections = new Array<Connection<Node>>();
	
	
	public Node() {
		
	}
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public Array<Connection<Node>> getConnections() {
		return connections;
	}
	
	public double slopeCost(Node other) {
		return Math.abs(this.slope - other.slope);
	}
	
	public double stepCost(Node other) {
		return Math.abs(this.z - other.z);
	}


}