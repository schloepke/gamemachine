package com.game_machine.persistence;

import java.util.concurrent.TimeUnit;

import com.game_machine.Config;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

public class WriteBehindPersistence extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private WriteBehindHandler handler;

	public WriteBehindPersistence() throws ClassNotFoundException {

		Class<?> store = Class.forName(Config.objectStore);
		ActorRef storeRef = this.getContext().actorOf(Props.create(store).withRouter(new RoundRobinRouter(10)), store.getSimpleName());
		handler = new WriteBehindHandler(storeRef, 5000, 50);
		
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
				handler.checkQueue();
			}
		} else {
			unhandled(message);
		}
	}
}
