package com.game_machine.systems;

import com.google.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Serialization extends UntypedActor {

	private ActorRef entryClass = null;
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	
	public Serialization(ActorRef master) {
		this.entryClass = this.getContext().actorOf(new Props(new UntypedActorFactory() {
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
