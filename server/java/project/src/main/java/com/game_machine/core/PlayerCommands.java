package com.game_machine.core;

import akka.actor.ActorSelection;
import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.GameMessages;
import GameMachine.Messages.Player;

public class PlayerCommands {

	public static void sendGameMessage(GameMessage gameMessage, String playerId) {
		GameMessages gameMessages = new GameMessages();
		gameMessages.addGameMessage(gameMessage);
		Entity entity = new Entity();
		entity.setId("0");
		entity.setGameMessages(gameMessages);
		PlayerCommands.sendToPlayer(entity, playerId);
	}
	
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
