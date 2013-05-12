package com.game_machine.game;

import java.util.ArrayList;

import akka.actor.UntypedActor;

import com.game_machine.Entities;
import com.game_machine.Entity;

public class Echo extends UntypedActor {

	public void onReceive(Object message) {
		if (message instanceof Entity) {
			Entity entity = (Entity) message;
			Entities entities = new Entities();
			entities.addEntity(entity);
			Gateway.sendToClient(entity.getClientId(),entities.toComponents().toByteArray());
		} else {
			unhandled(message);
		}
	}
}
