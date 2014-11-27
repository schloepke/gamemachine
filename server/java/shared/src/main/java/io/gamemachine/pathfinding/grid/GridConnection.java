package io.gamemachine.pathfinding.grid;

import com.badlogic.gdx.ai.pfa.Connection;

public class GridConnection<N> implements Connection<N> {

	protected N fromNode;
	protected N toNode;
	public float cost = 1f;

	public GridConnection(N fromNode, N toNode) {
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	@Override
	public float getCost() {
		return cost;
	}

	@Override
	public N getFromNode() {
		return fromNode;
	}

	@Override
	public N getToNode() {
		return toNode;
	}

}