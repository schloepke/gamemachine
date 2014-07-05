package com.game_machine.core;

import java.util.logging.Logger;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class GameMachineLoader {

	

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GameMachineLoader.class.getName());
	private static ActorSystem actorSystem;

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public static void StartMessageGateway() {
		actorSystem.actorOf(new RoundRobinPool(400).props(Props.create(MessageGateway.class)), 
			    MessageGateway.name);
	}
	
	public static void StartEntityTracking() {
		actorSystem.actorOf(new RoundRobinPool(400).props(Props.create(EntityTracking.class)), 
			    EntityTracking.name);
	}
	
	public void run(ActorSystem newActorSystem, String gameHandler) {
		Thread.currentThread().setName("game-machine");
		actorSystem = newActorSystem;
		actorSystem.actorOf(Props.create(EventStreamHandler.class), EventStreamHandler.class.getSimpleName());
	}

}
