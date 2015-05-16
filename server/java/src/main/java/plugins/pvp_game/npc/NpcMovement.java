package plugins.pvp_game.npc;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import plugins.pvp_game.Common;
import plugins.pvp_game.pathfinding.Graph;
import plugins.pvp_game.pathfinding.Node;
import plugins.pvp_game.pathfinding.PathResult;
import akka.event.LoggingAdapter;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PathData;
import io.gamemachine.messages.TrackData;
import io.gamemachine.util.Vector3;

public class NpcMovement {

	public String id;
	private String targetId;
	public Vector3 target;
	private Vector3 pathTarget;
	private Vector3 position;
	private long lastUpdate = 0;
	private double walkSpeed = 2.8f;
	private double runSpeed = 5f;
	public double speed = walkSpeed;
	public boolean hasTarget = false;
	public boolean reachedTarget = false;
	private LoggingAdapter logger;
	private Queue<Vector3> pathQueue = new LinkedList<Vector3>();
	private boolean movingTarget = false;
	private boolean isPlayer = false;
	private int resetPathCount = 0;
	private Grid grid;
	private int moveCount = 0;
	private long lastPathRequest;
	
	public NpcMovement(String id, Vector3 position, LoggingAdapter logger, Grid grid) {
		this.id = id;
		this.position = position;
		this.logger = logger;
		this.grid = grid;
	}

	public void update() {
		if (target == null) {
			return;
		}
		
		if (hasReachedTarget(target)) {
			setTargetReached();
			return;
		}
		
		if (resetPathCount >= 100) {
			requestRemotePath();
			resetPathCount = 0;
			if (isPlayer) {
				logger.warning("set_player_target");
				TrackData trackData = grid.get(targetId);
				target = Common.toVector3(trackData.x, trackData.y);
			}
		}
		
		setPathTarget();
		if (pathTarget == null) {
			//logger.warning(id + " unable to get path");
			return;
		}
		
		
		move();
		
		if (movingTarget) {
			resetPathCount++;
		}
		
		lastUpdate = System.currentTimeMillis();
	}

	
	private void setTarget(Vector3 target) {
		this.target = target;
		lastUpdate = System.currentTimeMillis();
	}
	
	public void setWaypointTarget(Vector3 target) {
		if (this.target != null && this.target.isEqualTo(target)) {
			return;
		}
		
		clearTarget();
		this.movingTarget = false;
		setTarget(target);
	}
		
	public void setPlayerTarget(String targetId) {
		if (this.targetId != null && this.targetId.equals(targetId)) {
			return;
		}
		clearTarget();
		movingTarget = true;
		isPlayer = true;
		this.targetId = targetId;
		TrackData trackData = grid.get(targetId);
		Vector3 target = Common.toVector3(trackData.x, trackData.y);
		setTarget(target);
	}
	
	public void setNpcTarget(String targetId) {
		if (this.targetId != null && this.targetId.equals(targetId)) {
			return;
		}
		clearTarget();
		movingTarget = true;
		this.targetId = targetId;
		setTarget(NpcManager.positions.get(this.targetId));
	}
	
	private Vector3 nextPath() {
		if (pathQueue.size() > 0) {
			return pathQueue.remove();
		} else {
			requestRemotePath();
			return null;
		}
	}
	
	private void setPathTarget() {
		if (pathTarget == null) {
			pathTarget = nextPath();
			if (pathTarget == null) {
				return;
			}
		}
		
		if (hasReachedTarget(pathTarget)) {
			pathTarget = nextPath();
		}
	}

	private boolean hasReachedTarget(Vector3 targ) {
		double distanceToTarget = position.distance2d(targ);
		if (distanceToTarget <= 1) {
			return true;
		} else {
			return false;
		}
	}

	private void clearTarget() {
		target = null;
		pathTarget = null;
		pathQueue.clear();
		movingTarget = false;
		targetId = null;
		reachedTarget = false;
		isPlayer = false;
	}
	
	private void setTargetReached() {
		if (reachedTarget) {
			return;
		}

		reachedTarget = true;
		pathTarget = null;
		if (!movingTarget) {
			target = null;
		}
		
		pathQueue.clear();
		logger.warning(id + " reached target");
	}

	private void move() {

//		 if (moveCount >= 20 && (id.equals("npc_guard4") || id.equals("npc_guard1"))) {
//		 logger.warning(targetId+" "+position.toString()+ " "+pathTarget.toString());
//		 moveCount = 0;
//		 }
		 
		 moveCount++;

		 Vector3 dir = Vector3.zero();
		dir.x = pathTarget.x - position.x;
		dir.y = pathTarget.y - position.y;
		dir.normalizeLocal();

		double deltatime = deltaTime();
		
		position.x += dir.x * speed * deltatime;
		position.y += dir.y * speed * deltatime;
	}

	private double deltaTime() {
		return (System.currentTimeMillis() - lastUpdate) / 1000d;
	}

	private void requestRemotePath() {
		if ((System.currentTimeMillis() - lastPathRequest) < 1000l) {
			return;
		}
		lastPathRequest = System.currentTimeMillis();
		
		GameMessage msg = new GameMessage();
		msg.pathData = new PathData();
		msg.pathData.id = id;
		msg.pathData.startPoint = new io.gamemachine.messages.Vector3();
		msg.pathData.startPoint.x = (float) position.x;
		msg.pathData.startPoint.y = (float) position.y;
		msg.pathData.startPoint.z = (float) position.z;
		msg.pathData.endPoint = new io.gamemachine.messages.Vector3();
		msg.pathData.endPoint.x = (float) target.x;
		msg.pathData.endPoint.y = (float) target.y;
		msg.pathData.endPoint.z = (float) target.z;
		PlayerCommands.sendGameMessage(msg, "unity1");
	}
	
	public void setPath(List<io.gamemachine.messages.Vector3> path) {
		pathQueue.clear();
		for (io.gamemachine.messages.Vector3 node : path) {
			Vector3 vec = new Vector3(node.x,node.y,node.z);
			pathQueue.add(vec);
		}
		logger.warning("got path for "+id);
	}
	
	private void setPath() {
		pathQueue.clear();
		pathTarget = null;
		PathResult result = Graph.graph.findPath(position, target, true);
		if (result.result) {
			if (result.smoothPathCount > 0) {
				
				for (Vector3 vec : result.smoothPath) {
					pathQueue.add(vec);
				}
			} else {
				for (int i = 0; i < result.resultPath.getCount(); i++) {
					Node node = (Node) result.resultPath.get(i);
					Vector3 vec = new Vector3(node.x, node.y, 0f);
					pathQueue.add(vec);
				}
			}
			
		} else {
			logger.warning(id + " Path error " + result.error+" position "+position.toString()+" target "+target.toString());
		}
		pathQueue.add(target);
	}

	public boolean isMoving() {
		return (pathTarget != null);
	}

	public boolean isWalking() {
		return (isMoving() && speed == walkSpeed);
	}

	public boolean isRunning() {
		return (isMoving() && speed == runSpeed);
	}

}