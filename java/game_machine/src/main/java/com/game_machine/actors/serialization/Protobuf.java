package com.game_machine.actors.serialization;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Protobuf extends UntypedActor {

LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			log.info("ROUTER.INCOMING String message: {}", message);
		} else {
			unhandled(message);
		}
	}
}
