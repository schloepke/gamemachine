package com.game_machine.core;
import GameMachine.Messages.ClientManagerRegister;
import GameMachine.Messages.Entity;
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
