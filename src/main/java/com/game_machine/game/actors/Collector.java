package com.game_machine.game.actors;

import akka.actor.UntypedActor;

import com.game_machine.messages.NetMessage;

public class Collector extends UntypedActor {

	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			
		}
	}
}
