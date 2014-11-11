package user;

import java.util.Random;

import io.gamemachine.client.api.ClientGrid;
import io.gamemachine.util.Vector3;

public class Npc {

	public Vector3 position = Vector3.zero();
	private Vector3 target = Vector3.zero();
	private ClientGrid grid;
	private NpcMovement movement;
	public String id;

	public Npc(String id, ClientGrid grid, double speedScale) {
		this.grid = grid;
		this.id = id;
		setSpawnPoint();
		this.movement = new NpcMovement(position, speedScale);
	}

	private float rand(int num) {
		return (float) (Math.random() * num + 1);
	}

	private float randRange(int start, int end) {
		Random r = new Random();
		return (float) r.nextInt(end - start) + start;
	}

	private void setSpawnPoint() {
		position.x = rand(grid.getMax());
		position.y = rand(grid.getMax());
	}

	private void pickTarget() {
		target.x = position.x + randRange(-30, 30);
		target.y = position.y + randRange(-30, 30);
		if (target.x < 2 || target.x > grid.getMax()) {
			target.x = randRange(5, grid.getMax());
		}
		if (target.y < 2 || target.y > grid.getMax()) {
			target.y = randRange(5, grid.getMax());
		}
		movement.setTarget(target);
	}

	public void update() {
		if (movement.hasTarget) {
			if (movement.reachedTarget) {
				pickTarget();
			}
		} else {
			pickTarget();
		}

		movement.update();
	}
}
