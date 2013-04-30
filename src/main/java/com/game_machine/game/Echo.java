package com.game_machine.game;

import akka.actor.UntypedActor;

public class Echo extends UntypedActor {

	public void onReceive(Object message) {
		this.getSender().tell(message,null);
	}
}
