package io.gamemachine.core;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubMediator;
import scala.concurrent.duration.Duration;
import io.gamemachine.messages.CharacterNotification;
import io.gamemachine.messages.ClientManagerEvent;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerNotification;

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
		} else if (message instanceof PlayerNotification) {
			PlayerNotification playerNotification = (PlayerNotification)message;
			onPlayerNotification(playerNotification);
		} else if (message instanceof CharacterNotification) {
			CharacterNotification characterNotification = (CharacterNotification)message;
			onCharacterNotification(characterNotification);
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
	
	public void onTick(String message) {}

	public final void sendGameMessage(GameMessage gameMessage, String playerId) {
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	public void onPlayerDisconnect(String playerId){}
	
	public void onPlayerNotification(PlayerNotification playerNotification) {}
	
	public void onCharacterNotification(CharacterNotification characterNotification) {}
	
	public final void subscribeToPlayerNotifications() {
		ActorRef ref = ChatMediator.getInstance().get(PlayerService.channel);
		ref.tell(new DistributedPubSubMediator.Subscribe(PlayerService.channel,getSelf()),getSelf());
	}
	
	public final void subscribeToForCharacterNotifications() {
		ActorRef ref = ChatMediator.getInstance().get(CharacterService.channel);
		ref.tell(new DistributedPubSubMediator.Subscribe(CharacterService.channel,getSelf()),getSelf());
	}
	
	public final void scheduleOnce(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
						null);
	}
	
	public final void scheduleOnce(long delay, GameMessage message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
						null);
	}
	
}
