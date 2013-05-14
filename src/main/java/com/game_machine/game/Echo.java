package com.game_machine.game;

import akka.actor.UntypedActor;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.Entity;

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
