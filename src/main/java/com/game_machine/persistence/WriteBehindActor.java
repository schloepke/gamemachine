package com.game_machine.persistence;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

public class WriteBehindActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private WriteBehindHandler handler;

	public WriteBehindActor() {
		handler = new WriteBehindHandler(5000, 50);
		this.getContext().actorOf(Props.create(RiakStore.class).withRouter(new RoundRobinRouter(10)), RiakStore.class.getSimpleName());
		this.getContext()
				.system()
				.scheduler()
				.schedule(Duration.Zero(), Duration.create(handler.getMinTimeBetweenWrites(), TimeUnit.MILLISECONDS), this.getSelf(),
						"tick", this.getContext().system().dispatcher(), null);
	}

	public void onReceive(Object message) {
		if (message instanceof GameObject) {
			log.info("GAMEOBJECT");
			handler.write(message);
		} else if (message instanceof String) {
			if (message.equals("tick")) {
				handler.write(message);
			}
		} else {
			unhandled(message);
		}
	}
}
