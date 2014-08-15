package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.GameMessages;
import akka.actor.UntypedActor;

public class GameActor extends UntypedActor {

	private static ConcurrentHashMap<String, GameMessage> reliableMessages = new ConcurrentHashMap<String, GameMessage>();
	private static ConcurrentHashMap<String, Integer> reliableMessageStatus = new ConcurrentHashMap<String, Integer>();

	private String playerId;

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
		if (reliableMessageStatus.containsKey(gameMessage.messageId)) {
			reliableMessages.put(gameMessage.messageId, gameMessage);
			reliableMessageStatus.replace(gameMessage.messageId,1);
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
				return true;
			}
		}
		return false;
	}
}
