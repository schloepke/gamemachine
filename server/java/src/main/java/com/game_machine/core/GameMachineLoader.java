package com.game_machine.core;

import java.util.logging.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class GameMachineLoader {

	

	private static final Logger log = Logger.getLogger(GameMachineLoader.class.getName());
	private static ActorSystem actorSystem;

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public void run(ActorSystem newActorSystem, String gameHandler) {
		Thread.currentThread().setName("game-machine");
		actorSystem = newActorSystem;
		actorSystem.actorOf(Props.create(EventStreamHandler.class), EventStreamHandler.class.getSimpleName());
	
		actorSystem.actorOf(new RoundRobinPool(10).props(Props.create(MessageGateway.class)), 
				    "message_gateway");
	}

}
