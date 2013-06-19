package com.game_machine.core;

import akka.actor.ActorSelection;
import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class EventStreamHandler extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public EventStreamHandler() {
		log.error("subscribing to deadletter");
		this.getContext().system().eventStream().subscribe(this.getSelf(), DeadLetter.class);
	}
	@Override
	public void onReceive(Object message) throws Exception {
		log.error(message.toString());
		ActorSelection sel = ActorUtil.getSelectionByName("GameMachine::SystemMonitor");
		sel.tell(message,this.getSelf());
		
	}
}
