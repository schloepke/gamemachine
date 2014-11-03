package user;

import Client.Messages.TrackData;

import com.game_machine.client.api.ClientGrid;
import com.game_machine.util.Vector3;

public class NpcMovement {

	private ClientGrid grid;
	private Vector3 target;
	private Vector3 position;
	private double distanceToTarget = 0;
	private double lastMove = 0;
	private double speedScale = 4;
	private boolean positionChanged = false;
	public boolean hasTarget = false;
	private int updatesForMove = 0;
	private int updatesBetweenMove = 5;
	public boolean reachedTarget = false;
	private String id;

	public NpcMovement(String id, Vector3 position, ClientGrid grid) {
		this.grid = grid;
		this.id = id;
		this.position = position;
		this.lastMove = System.currentTimeMillis() / 1000l;
	}

	public void update() {
		positionChanged = false;
		if (hasTarget && !reachedTarget) {
			if (updatesForMove >= updatesBetweenMove) {
				move();
				updatesForMove = 0;
			}
		} else {
			return;
		}

		if (positionChanged) {
			//System.out.println(id+" "+position.toString());
			grid.set(id, (float) position.x, (float) position.y, 0, TrackData.EntityType.NPC);
			positionChanged = false;
		}
		updatesForMove++;
	}

	private void targetReached() {
		//System.out.println("Target reached "+target.toString());
		position.x = target.x;
		position.y = target.y;
		reachedTarget = true;
	}
	
	public void setTarget(Vector3 newTarget) {
		target = newTarget;
		lastMove = System.currentTimeMillis() / 1000l;
		hasTarget = true;
		reachedTarget = false;
	}
	
	private void dropTarget() {
		target = null;
		hasTarget = false;
		reachedTarget = false;
		distanceToTarget = 0;
	}

	private void move() {
		distanceToTarget = position.distance2d(target);
		if (distanceToTarget <= 1) {
			targetReached();
			return;
		}
		double deltaTime = (System.currentTimeMillis() / 1000l) - lastMove;

		Vector3 dist = Vector3.zero();
		dist.x = target.x - position.x;
		dist.y = target.y - position.y;
		dist.normalizeLocal();

		position.x += dist.x * speedScale * deltaTime;
		position.y += dist.y * speedScale * deltaTime;

		// we moved past the target
		if (position.distance2d(target) > distanceToTarget) {
			targetReached();
		}

		lastMove = System.currentTimeMillis() / 1000l;
		positionChanged = true;
	}

}
