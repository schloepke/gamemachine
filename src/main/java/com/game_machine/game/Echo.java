package com.game_machine.game;

import java.util.ArrayList;

import akka.actor.UntypedActor;

import com.game_machine.proto.Entities;
import com.game_machine.proto.Entity;

public class Echo extends UntypedActor {

	public void onReceive(Object message) {
		if (message instanceof Entity) {
			Entity entity = (Entity) message;
			Entities entities = new Entities();
			ArrayList<Entity> entityList = new ArrayList<Entity>();
			entityList.add(entity);
			entities.setEntityList(entityList);
			Gateway.sendToClient(entity.getClientId(),entities.toByteArray());
		} else {
			unhandled(message);
		}
	}
}
