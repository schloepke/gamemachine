package plugins.pvp_game;

import java.util.concurrent.TimeUnit;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.GridConfig;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridManager;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TimeCycle;
import io.gamemachine.messages.TrackData;
import scala.concurrent.duration.Duration;

public class TimeHandler extends UntypedActor {

	public static String name = TimeHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	public long starts;

	@Override
	public void onReceive(Object message) throws Exception {
		GameMessage msg = new GameMessage();
		TimeCycle timeCycle = new TimeCycle();
		timeCycle.currentTime = (float) time(TimeUnit.SECONDS) % 600f;
		timeCycle.dayCycleLength = 600f;
		msg.timeCycle = timeCycle;

		for (Grid grid : GridManager.getGrids()) {
			if (grid.getType() != GridConfig.GridType.Moving) {
				continue;
			}
			
			for (TrackData trackData : grid.getAll()) {
				if (trackData.entityType != TrackData.EntityType.Player) {
					continue;
				}
				PlayerMessage.tell(msg, trackData.id);
			}
		}
		
		tick(2000L, "tick");
	}

	public void reset() {
		starts = System.currentTimeMillis();
	}

	public long time() {
		long ends = System.currentTimeMillis();
		return ends - starts;
	}

	public long time(TimeUnit unit) {
		return unit.convert(time(), TimeUnit.MILLISECONDS);
	}

	public void preStart() {
		reset();
		tick(3000L, "tick");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
}
