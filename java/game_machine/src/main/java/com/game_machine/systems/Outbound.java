package com.game_machine.systems;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.GameMessage;

public class Outbound extends UntypedActor {

	public Outbound() {
	}

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);


	public void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			//this.getContext().child("game").tell(message, this.getSelf());
			// log.info("Router ClientMsg");
			// ClientMsg msg = (ClientMsg) message;
			// echo it back for now
			// server.sendMessage(msg.getBody().toStringUtf8(),
			// msg.getHostname(), msg.getPort());
		} else {
			unhandled(message);
		}
	}
}
