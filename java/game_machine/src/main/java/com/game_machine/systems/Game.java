package com.game_machine.systems;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;
import com.game_machine.server.Server;

public class Game extends UntypedActor {

LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Game() {
		
	}
	
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			log.info("Game GameMessage message: {}", message);
			ActorSelection ref = this.getContext().actorSelection("/user/outbound");
			ref.tell(message, this.getSelf());
		} else {
			unhandled(message);
		}
	}
}
