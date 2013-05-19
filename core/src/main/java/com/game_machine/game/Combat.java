package com.game_machine.game;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Combat extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Combat() {
		
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			// get all the players we hit
			// calculate dmg to each player
		} else {
			unhandled(message);
		}
	}
}
