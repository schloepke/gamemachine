package com.game_machine.core.game;

import java.util.HashMap;

import com.game_machine.core.GatewayMessage;
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.GameCommand;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GameCommandRouter extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<String, ActorRef> actors = new HashMap<String, ActorRef>();

	public GameCommandRouter() {
		actors.put(Combat.class.getSimpleName(), this.getContext().actorOf(Props.create(Combat.class), Combat.class.getSimpleName()));
		actors.put(Location.class.getSimpleName(), this.getContext().actorOf(Props.create(Location.class), Location.class.getSimpleName()));
		actors.put(Echo.class.getSimpleName(), this.getContext().actorOf(Props.create(Echo.class), Echo.class.getSimpleName()));
	}

	public void onReceive(Object message) {
		log.info("GameCommandRouter got message " + message.toString());
		if (message instanceof GatewayMessage) {
			GatewayMessage gatewayMessage = (GatewayMessage) message;
			Components components = Components.parseFrom(gatewayMessage.getBytes());
			Entities entities = components.toEntities();
			for (Entity entity : entities.getEntities().values()) {
				entity.setClientId(gatewayMessage.getClientId());
				GameCommand gameCommand = entity.getGameCommand();
				if (gameCommand != null) {
					log.info("GameCommand: " + gameCommand.getName());
					ActorRef commandHandler = actors.get(gameCommand.getName());
					if (commandHandler != null) {
						commandHandler.tell(entity, this.getSelf());
					}
				}

			}
		} else {
			unhandled(message);
		}
	}
}
