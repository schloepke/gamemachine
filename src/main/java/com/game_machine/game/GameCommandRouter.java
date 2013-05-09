package com.game_machine.game;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GameCommandRouter extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	
	public void onReceive(Object message) {
		
	}
}
