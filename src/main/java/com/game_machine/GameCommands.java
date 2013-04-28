package com.game_machine;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;

public class GameCommands extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
		} else if (message instanceof String) {
		} else {
			unhandled(message);
		}
	}
}
