package com.game_machine.core.game;

import scala.Option;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.core.GatewayMessage;
import com.game_machine.entity_system.Component;
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.Entity;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GameActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private String clientId;

	public String getClientId() {
		return clientId;
	}

	@Override
	public final void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage) message;
			clientId = gameMessage.getClientId();

			if (gameMessage.hasEntity()) {
				onReceive((Entity) gameMessage.getEntity());
			} else if (gameMessage.hasEntities()) {
				onReceive((Entities) gameMessage.getEntities());
			} else if (gameMessage.hasComponent()) {
				onReceive((Component) gameMessage.getComponent());
			}
		} else if (message instanceof GatewayMessage) {
			GatewayMessage gatewayMessage = (GatewayMessage) message;
			clientId = gatewayMessage.getClientId();

			Components components = Components.parseFrom(gatewayMessage.getBytes());
			Entities entities = components.toEntities();
			onReceive(entities);
		} else if (message instanceof String) {
			onReceive((String) message);
		} else {
			unhandled(message);
		}
	}

	public void onReceive(Entity entity) {
		throw new RuntimeException("How did I get here");
	}

	public void onReceive(Entities entities) {
		throw new RuntimeException("How did I get here");
	}

	public void onReceive(String command) {
		throw new RuntimeException("How did I get here");
	}

	public void onReceive(Component component) {
		throw new RuntimeException("How did I get here");
	}

	public void sendTo(String name, Entity entity) {
		ActorSelection selection  = this.getContext().actorSelection(name);
		GameMessage message = new GameMessage(clientId, entity, null, null);
		selection.tell(message, this.getSelf());
	}
}
