package io.gamemachine.core;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import io.gamemachine.messages.ClientManagerEvent;
import io.gamemachine.messages.GameMessage;

public abstract class GameMessageActor extends GameActor {

	public void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage) message;
			setPlayerId(gameMessage.playerId);
			onGameMessage(gameMessage);
		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent clientManagerEvent = (ClientManagerEvent) message;
			if (clientManagerEvent.event.equals("disconnected")) {
				onPlayerDisconnect(clientManagerEvent.player_id);
			}
		} else if (message instanceof String) {
			onTick((String)message);
		} else {
			unhandled(message);
		}
	}

	@Override
	public void preStart() {
		awake();
	}

	public abstract void awake();

	public abstract void onGameMessage(GameMessage gameMessage);
	
	public abstract void onTick(String message);

	public void sendGameMessage(GameMessage gameMessage, String playerId) {
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	public abstract void onPlayerDisconnect(String playerId);
	

	public void scheduleOnce(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
						null);
	}
	
	public void scheduleOnce(long delay, GameMessage message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
						null);
	}
	
}
