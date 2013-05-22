package com.game_machine.core.game;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.core.GatewayMessage;
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.Entity;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GameActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private GameMessage gameMessage;

	@Override
	public final void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			gameMessage = (GameMessage) message;

			if (gameMessage.hasEntity()) {
				onReceive((Entity) gameMessage.getEntity());
			} else if (gameMessage.hasEntities()) {
				onReceive((Entities) gameMessage.getEntities());
			}
		} else if (message instanceof GatewayMessage) {
			GatewayMessage gatewayMessage = (GatewayMessage) message;
			Components components = Components.parseFrom(gatewayMessage
					.getBytes());
			Entities entities = components.toEntities();
			gameMessage = new GameMessage(gatewayMessage.getClientId(), null,
					entities);
			onReceive(entities);
		} else {
			unhandled(message);
		}
	}

	public void onReceive(Entity entity) {

	}

	public void onReceive(Entities entities) {

	}

	public void sendToParent(Entity entity) {

	}

	public void sendToChild(String name, Entity entity) {

	}

	public void sendToClient(Entity entity) {
		Entities entities = new Entities();
		entities.addEntity(entity);
		byte[] bytes = Components.fromEntities(entities).toByteArray();
		GatewayMessage gatewayMessage = new GatewayMessage(bytes,
				gameMessage.getClientId());
		ActorUtil.getSelectionByClass(Gateway.class).tell(gatewayMessage, null);
	}

	public void sendTo(String name, Entity entity) {
		ActorSelection selection = ActorUtil.getSelectionByName(name);
		GameMessage message = new GameMessage(gameMessage.getClientId(),
				entity, gameMessage.getEntities());
		selection.tell(message, this.getSelf());
	}
}
