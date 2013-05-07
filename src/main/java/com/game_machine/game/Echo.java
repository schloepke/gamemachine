package com.game_machine.game;

import com.game_machine.ActorUtil;

import akka.actor.UntypedActor;

public class Echo extends UntypedActor {

	public void onReceive(Object message) {
		ActorUtil.getSelectionByClass(Gateway.class).tell(message, this.getSelf());
	}
}
