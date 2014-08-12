package com.game_machine.core;

import akka.actor.ActorSelection;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Player;

public class PlayerCommands {

	public static void sendToPlayer(Entity entity, String playerId) {
		if (!entity.hasPlayer()) {
			Player player = new Player();
			player.setId(playerId);
			entity.setPlayer(player);
		}
		entity.setSendToPlayer(true);
		
		ActorSelection sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		sel.tell(entity, null);
	}
}
