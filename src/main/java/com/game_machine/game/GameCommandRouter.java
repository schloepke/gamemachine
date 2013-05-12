package com.game_machine.game;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GatewayMessage;
import com.game_machine.MessageUtil;
import com.game_machine.proto.Entities;
import com.game_machine.proto.Entity;
import com.game_machine.proto.GameCommand;

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
			Entities entities = Entities.parseFrom(gatewayMessage.getBytes());
			log.info("JSON " +entities.toJson().toString());
			for (Entity entity : entities.getEntityList()) {
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
