package com.game_machine.core;

import GameMachine.Messages.Entity;
import akka.actor.UntypedActor;

public class RemoteEcho extends UntypedActor {

	public static String name = "GameMachine::GameSystems::RemoteEcho";

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Entity) {
			Entity entity = (Entity) message;
			PlayerCommands.sendToPlayer(entity, entity.getPlayer().getId());
		}

	}

}
