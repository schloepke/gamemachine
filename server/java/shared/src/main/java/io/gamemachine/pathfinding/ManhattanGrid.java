package io.gamemachine.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanGrid  implements Heuristic<Node> {

	@Override
	public float estimate (Node node, Node endNode) {
		//return (float) (Math.abs(endNode.position.x - node.position.x) + Math.abs(endNode.position.y - node.position.y));
		return (float) Math.abs(endNode.position.distance(node.position));
	}

}
