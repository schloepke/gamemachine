package io.gamemachine.core;

import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GameMessages;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackDataResponse;
import io.gamemachine.net.Connection;
import io.gamemachine.routing.GameMessageRoute;
import akka.actor.ActorSelection;

public class PlayerCommands {

	public static void SendLocal(GameMessage gameMessage, String destination) {
		if (GameMessageRoute.routes.containsKey(destination)) {
			GameMessageRoute route = GameMessageRoute.routes.get(destination);
			ActorSelection sel;
			if (route.isDistributed()) {
				sel = ActorUtil.findDistributed(route.getTo(), gameMessage.playerId);
			} else {
				sel = ActorUtil.getSelectionByName(route.getTo());
			}
			sel.tell(gameMessage, null);
		}
	}
	
	public static void sendGameMessage(GameMessage gameMessage, String playerId) {
		GameMessages gameMessages = new GameMessages();
		gameMessages.addGameMessage(gameMessage);
		Entity entity = new Entity();
		entity.setId("0");
		entity.setGameMessages(gameMessages);
		PlayerCommands.sendToPlayer(entity, playerId);
	}

	public static void sendTrackDataResponse(String playerId, String id, TrackDataResponse.REASON reason) {
		Entity entity = new Entity();
		entity.id = "0";
		TrackDataResponse response = new TrackDataResponse();
		response.id = id;
		response.reason = reason;
		entity.setTrackDataResponse(response);
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

		if (Connection.hasConnection(playerId)) {
			sel = ActorUtil.getSelectionByName(playerId);
		} else {
			sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		}

		sel.tell(entity, null);
	}

	// We need to refactor the client manager that's in ruby so we have a better way of getting at player info directly
	public static void disconnectPlayersForGame(String gameId) {
		for (String playerId : Connection.getConnectedPlayerIds()) {
			ActorSelection sel = ActorUtil.getSelectionByName(playerId);
			sel.tell(gameId, null);
		}
	}
}
