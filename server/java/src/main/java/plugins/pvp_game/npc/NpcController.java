package plugins.pvp_game.npc;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.messages.ClientData;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.NpcData;
import io.gamemachine.messages.TrackData;
import io.gamemachine.util.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plugins.pvp_game.Common;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class NpcController extends GameMessageActor {

	public long tickInterval = 40l;
	public static String name = NpcController.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private String id;
	private NpcData npcData;
	private Grid grid;
	private Vector3 position;
	private TrackData trackData;
	private int wpIndex = 0;
	private List<Vector3> waypoints = new ArrayList<Vector3>();
	public Vector3 currentWaypoint;
	private String patrolRoute;
	private NpcMovement npcMovement;
	private Vector3 leaderPos;

	public NpcController(NpcData npcData) {
		id = npcData.id;
		this.npcData = npcData;
		patrolRoute = npcData.patrolRoute;

		GameGrid.setPlayerZone(id, 1);
		grid = GameGrid.getGameGrid(Common.gameId, "default", id);

		initPosition();
		initTrackData();
		initWaypoints();

		npcMovement = new NpcMovement(id, position, logger,grid);
	}

	private void initPosition() {
		position = new Vector3();
		position.x = npcData.spawnpoint.x;
		position.y = npcData.spawnpoint.y;
		position.z = npcData.spawnpoint.z;
		NpcManager.positions.put(id, position);
	}

	private void initTrackData() {
		trackData = new TrackData();
		trackData.id = id;
		trackData.entityType = trackData.entityType.NPC;
		trackData.clientData = new ClientData();
	}

	private void initWaypoints() {
		if (npcData.hasWaypoint()) {
			for (io.gamemachine.messages.Vector3 pos : npcData.waypoint.position) {
				Vector3 vec = new Vector3(pos.x, pos.y, pos.z);
				waypoints.add(vec);
			}
			currentWaypoint = nextWaypoint();
		}
		if (hasWaypoints()) {
			logger.warning(id + " is a leader");
		}
	}

	private void postInit() {
		if (npcData.hasLeader()) {
			if (NpcManager.positions.containsKey(npcData.leader)) {
				leaderPos = NpcManager.positions.get(npcData.leader);
				logger.warning(id + " following " + npcData.leader);
			}
		}
	}

	@Override
	public void awake() {
		scheduleOnce(5000l, "postinit");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.hasPathData()) {
			npcMovement.setPath(gameMessage.pathData.nodes);
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {

	}

	@Override
	public void onTick(String message) {
		if (message.equals("postinit")) {
			postInit();
		}

		setTarget();
		npcMovement.update();

		sendTrackData();
		scheduleOnce(tickInterval, "tick");
	}

	private void setTarget() {
		if (hasWaypoints()) {
			if (npcMovement.reachedTarget) {
				npcMovement.setWaypointTarget(nextWaypoint());
			} else {
				npcMovement.setWaypointTarget(currentWaypoint);
			}
		} else if (hasLeader()) {
			npcMovement.setNpcTarget(npcData.leader);
		}
	}

	private List<String> neighbors() {
		return Common.getTargetsInRange(50, toInt(position.x), toInt(position.y), toInt(position.z), grid);
	}

	private int toInt(double num) {
		return (int) Math.round(num * 100l);
	}
	

	private void sendTrackData() {
		trackData.setX(toInt(position.x + Common.worldOffset));
		trackData.setY(toInt(position.y + Common.worldOffset));
		trackData.setZ(toInt(Common.worldOffset));
		trackData.setZone(1);
		if (npcMovement.isWalking()) {
			trackData.clientData.command2 = 2;
		} else if (npcMovement.isRunning()) {
			trackData.clientData.command2 = 3;
		} else {
			trackData.clientData.command2 = 1;
		}
		grid.set(trackData);
	}

	public boolean hasLeader() {
		return (leaderPos != null);
	}

	public boolean hasWaypoints() {
		return (waypoints.size() > 0);
	}

	public Vector3 nextWaypoint() {
		if (wpIndex >= waypoints.size()) {
			Collections.reverse(waypoints);
			wpIndex = 0;
			logger.warning(id+" reached waypoint end, reversing list");
		}

		currentWaypoint = waypoints.get(wpIndex);
		wpIndex++;
		return currentWaypoint;
	}
}
