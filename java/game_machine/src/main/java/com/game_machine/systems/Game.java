package com.game_machine.systems;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Game extends UntypedActor {

LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Game(ActorRef parent) {
		this.getContext().actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Example();
			}
		}), "entry");
	}
	
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			log.info("ROUTER.INCOMING String message: {}", message);
		} else {
			unhandled(message);
		}
	}
}
