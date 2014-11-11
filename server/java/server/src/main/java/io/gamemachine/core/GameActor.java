package io.gamemachine.core;

import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GameMessages;

import java.util.concurrent.ConcurrentHashMap;

import akka.actor.UntypedActor;

public class GameActor extends UntypedActor {

	private static ConcurrentHashMap<String, GameMessage> reliableMessages = new ConcurrentHashMap<String, GameMessage>();
	private static ConcurrentHashMap<String, Integer> reliableMessageStatus = new ConcurrentHashMap<String, Integer>();

	public String playerId;
	public String messageId;

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Override
	public void onReceive(Object message) throws Exception {

	}

	public static void removeReliableMessage(String id) {
		if (reliableMessages.containsKey(id)) {
			reliableMessages.remove(id);
		}

		if (reliableMessageStatus.containsKey(id)) {
			reliableMessageStatus.remove(id);
		}
	}

	public boolean setReply(GameMessage gameMessage) {
		gameMessage.messageId = this.messageId;
		if (reliableMessageStatus.containsKey(gameMessage.messageId)) {
			reliableMessages.put(gameMessage.messageId, gameMessage);
			reliableMessageStatus.replace(gameMessage.messageId, 1);
			sendReply(gameMessage.messageId);
			return true;
		} else {
			return false;
		}
	}

	private void sendReply(String messageId) {
		GameMessage gameMessage = reliableMessages.get(messageId);
		Entity entity = new Entity();
		entity.setId(messageId);
		entity.setGameMessages(new GameMessages());
		entity.gameMessages.addGameMessage(gameMessage);
		PlayerCommands.sendToPlayer(entity, playerId);
	}

	public boolean exactlyOnce(GameMessage gameMessage) {
		if (gameMessage.hasMessageId()) {
			if (reliableMessageStatus.containsKey(gameMessage.messageId)) {
				int status = reliableMessageStatus.get(gameMessage.messageId);
				if (status == 1) {
					sendReply(gameMessage.messageId);
				}
			} else {
				reliableMessageStatus.put(gameMessage.messageId, 0);
				this.messageId = gameMessage.messageId;
				return true;
			}
		}
		return false;
	}
}
