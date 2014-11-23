package io.gamemachine.pathfinding;


import io.gamemachine.util.Vector3;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class Node implements IndexedNode<Node>{

	public int index;
	public Vector3 position;
	public Array<Connection<Node>> connections = new Array<Connection<Node>>();
	
	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public Array<Connection<Node>> getConnections() {
		// TODO Auto-generated method stub
		return connections;
	}

	
	

}
