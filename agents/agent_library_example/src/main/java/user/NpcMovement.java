package user;

import com.game_machine.util.Vector3;

public class NpcMovement {

	private Vector3 target;
	private Vector3 position;
	private double distanceToTarget = 0;
	private double lastMove = 0;
	private double speedScale = 4f;
	public boolean hasTarget = false;
	public boolean reachedTarget = false;

	public NpcMovement(Vector3 position, double speedScale) {
		this.speedScale = speedScale;
		this.position = position;
		this.lastMove = System.currentTimeMillis();
	}

	public void update() {
		if (hasTarget && !reachedTarget) {
			move();
		} else {
			return;
		}
	}

	private void targetReached() {
		// System.out.println("Target reached "+target.toString());
		position.x = target.x;
		position.y = target.y;
		reachedTarget = true;
	}

	public void setTarget(Vector3 newTarget) {
		target = newTarget;
		lastMove = System.currentTimeMillis();
		hasTarget = true;
		reachedTarget = false;
	}

	private void move() {
		distanceToTarget = position.distance2d(target);
		if (distanceToTarget <= 1) {
			targetReached();
			return;
		}
		double deltaTime = (System.currentTimeMillis() - lastMove) / 1000f;

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

		lastMove = System.currentTimeMillis();
	}

}
