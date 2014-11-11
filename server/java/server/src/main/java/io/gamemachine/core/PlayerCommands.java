package io.gamemachine.core;

import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GameMessages;
import io.gamemachine.messages.Player;
import io.gamemachine.routing.Incoming;
import akka.actor.ActorSelection;

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

		ActorSelection sel;
		
		if (Incoming.hasClient(playerId)) {
			sel = ActorUtil.getSelectionByName(playerId);
		} else {
			sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		}
		
		sel.tell(entity, null);
	}
}
