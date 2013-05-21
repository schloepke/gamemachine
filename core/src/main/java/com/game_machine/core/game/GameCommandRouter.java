package com.game_machine.core.game;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.GameCommand;

public class GameCommandRouter extends GameActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<String, ActorRef> actors = new HashMap<String, ActorRef>();

	public GameCommandRouter() {
		actors.put(
				Combat.class.getSimpleName(),
				this.getContext().actorOf(Props.create(Combat.class),
						Combat.class.getSimpleName()));
		actors.put(
				Location.class.getSimpleName(),
				this.getContext().actorOf(Props.create(Location.class),
						Location.class.getSimpleName()));
		actors.put(
				Echo.class.getSimpleName(),
				this.getContext().actorOf(Props.create(Echo.class),
						Echo.class.getSimpleName()));
	}

	public void onReceive(Entities entities) {
		for (Entity entity : entities.getEntities().values()) {
			if (entity.hasGameCommand()) {
				GameCommand gameCommand = entity.getGameCommand();
				log.info("GameCommand: " + gameCommand.getName());
				sendTo(gameCommand.getName(),entity);
			}
		}
	}
}
