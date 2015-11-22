package io.gamemachine.grid;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;

public class GridExpiration extends UntypedActor {
	
	public static String name = GridExpiration.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			for (Grid grid : GameGrid.gridsStartingWith("default")) {
				grid.RemoveExpired(AppConfig.getGridExpiration());
			}
			tick(AppConfig.getGridExpiration(), "tick");
		}
	}

		
	@Override
	public void preStart() {
		tick(AppConfig.getGridExpiration(), "tick");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
