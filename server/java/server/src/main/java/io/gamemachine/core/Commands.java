package io.gamemachine.core;

import io.gamemachine.messages.ClientManagerRegister;
import io.gamemachine.messages.Entity;
import akka.actor.ActorSelection;

public class Commands {

	public static void clientManagerRegister(String name) {
		ClientManagerRegister register = new ClientManagerRegister();
		register.setRegisterType("actor").setName(name);
		register.setEvents("");
		Entity entity = new Entity();
		entity.setClientManagerRegister(register);
		ActorSelection sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		sel.tell(entity, null);
	}

}
