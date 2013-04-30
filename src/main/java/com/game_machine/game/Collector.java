package com.game_machine.game;

import akka.actor.UntypedActor;

import com.game_machine.NetMessage;

public class Collector extends UntypedActor {

	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			
		}
	}
}
