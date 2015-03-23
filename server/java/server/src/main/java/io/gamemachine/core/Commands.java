package io.gamemachine.core;

import java.util.List;

import io.gamemachine.messages.AddPlayerItem;
import io.gamemachine.messages.ClientManagerRegister;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.RemovePlayerItem;
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
	
	public static List<io.gamemachine.messages.Character> getCharacters() {
		return io.gamemachine.messages.Character.db().findAll();
	}
}
