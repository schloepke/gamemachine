package pvp_game;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GridExpiration extends UntypedActor {
	
	public static String name = GridExpiration.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			Grid grid = GameGrid.getGameGrid("mygame", "default");
			grid.RemoveExpired(10000L);
			tick(10000L, "tick");
		}
	}

		
	@Override
	public void preStart() {
		tick(10000L, "tick");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
