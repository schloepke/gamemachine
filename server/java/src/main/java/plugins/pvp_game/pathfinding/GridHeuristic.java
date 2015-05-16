package plugins.pvp_game.pathfinding;

import io.gamemachine.util.Vector3;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class GridHeuristic  implements Heuristic<Node> {

	@Override
	public float estimate (Node node, Node endNode) {
		if (Graph.heuristic == Graph.Heuristic.MANHATTAN) {
			return (float) Math.abs(endNode.position.distance(node.position));
		} else if (Graph.heuristic == Graph.Heuristic.EUCLIDEAN) {
			return (float) getEuclideanCost(node.position,endNode.position);
		} else {
			return (float) (Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y));
		}
	}
	
	public double getEuclideanCost(Vector3 node, Vector3 target) {
		double dx = target.x - node.x;
		double dy = target.y - node.y;
		double dz = target.z - node.z;
		
		return 1.0 * Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

}
