package com.game_machine.core;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.GameMessage;

public class GameMessageActor extends GameActor {

	
	public void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage)message;
			setPlayerId(gameMessage.playerId);
			onGameMessage(gameMessage);
		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent clientManagerEvent = (ClientManagerEvent)message;
			if (clientManagerEvent.event.equals("disconnected")) {
				onPlayerDisconnect(clientManagerEvent.player_id);
			}
		} else {
			unhandled(message);
		}
	}
	
	@Override
	public void preStart() {
		awake();
	}
	
	public void awake() {
		
	}
	
	public void onGameMessage(GameMessage gameMessage) {
		
	}
	
	public void sendGameMessage(GameMessage gameMessage, String playerId) {
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}
	
	private void onPlayerDisconnect(String playerId) {
		
	}
}
