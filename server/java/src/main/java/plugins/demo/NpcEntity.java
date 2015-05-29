package plugins.demo;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.messages.UserDefinedData;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.TrackData.EntityType;
import io.gamemachine.util.Vector3;
import java.util.Random;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class NpcEntity extends GameMessageActor {

	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	private int worldOffset = 1;
	private String id;
	private Grid grid;
	private Vector3 position;
	private TrackData trackData;
	public long tickInterval = 40l;
	public float width = 490f;
	public double speed = 6d;
	public Vector3 target;
	private Random rand;
	private long lastUpdate = 0;

	public NpcEntity(String playerId, String characterId) {
		id = playerId;
	}

	@Override
	public void awake() {
		rand = new Random();
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default", id);
		position = randVector();
		target = randVector();
		initTrackData();
		lastUpdate = System.currentTimeMillis();
		
		scheduleOnce(tickInterval, "update");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
	}
	
	@Override
	public void onTick(String message) {
		move();
		double dist = position.distance2d(target);
		if (dist <= 2) {
			target = randVector();
		}
		lastUpdate = System.currentTimeMillis();
		sendTrackData();
		scheduleOnce(tickInterval, "update");
	}
	
	private void move() {
		double deltatime = deltaTime();
		Vector3 dir = Vector3.zero();
		dir.x = target.x - position.x;
		dir.y = target.y - position.y;
		dir.normalizeLocal();
		
		position.x += (dir.x * speed * deltatime);
		position.y += (dir.y * speed * deltatime);
	}

	private double deltaTime() {
		return (System.currentTimeMillis() - lastUpdate) / 1000d;
	}

	private void initTrackData() {
		trackData = new TrackData();
		trackData.id = id;
		trackData.entityType = EntityType.NPC;
		trackData.userDefinedData = new UserDefinedData();
	}

	private int toInt(double num) {
		return (int) Math.round(num * 100l);
	}
	
	private void sendTrackData() {
		trackData.setX(toInt(position.x + worldOffset));
		trackData.setY(toInt(position.y + worldOffset));
		trackData.setZ(100);

		grid.set(trackData);
	}

	private Vector3 randVector() {
		Vector3 np = new Vector3();
		np.x = randFloat(10f, width);
		np.y = randFloat(10f, width);
		np.z = 1f;
		return np;
	}

	private float randFloat(float min, float max) {
		return rand.nextInt((int) ((max - min) + 1)) + min;
	}
}
