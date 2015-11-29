package io.gamemachine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GameMessages;
import io.gamemachine.messages.Player;
import io.gamemachine.net.Connection;

public class PlayerMessage {

	private static final Logger logger = LoggerFactory.getLogger(PlayerMessage.class);
		
	public static void tell(GameMessage gameMessage, String playerId) {
		GameMessages gameMessages = new GameMessages();
		gameMessages.addGameMessage(gameMessage);
		Entity entity = new Entity();
		entity.setId("0");
		entity.setGameMessages(gameMessages);
		PlayerMessage.tell(entity, playerId);
	}


	public static void tell(Entity entity, String playerId) {
		if (entity.player == null) {
			Player player = new Player();
			player.setId(playerId);
			entity.setPlayer(player);
		}
		entity.setSendToPlayer(true);

		ActorSelection sel;

		if (Connection.hasConnection(playerId)) {
			sel = ActorUtil.getSelectionByName(playerId);
		} else {
			sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		}

		sel.tell(entity, null);
	}

}
