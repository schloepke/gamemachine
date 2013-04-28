package com.game_machine.systems;

import akka.actor.UntypedActor;

public class ServerMonitor extends UntypedActor {

	
	public void onReceive(Object message) {
		this.getSender().tell("OK", this.getSelf());
	}
}
