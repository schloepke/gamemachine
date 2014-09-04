package com.game_machine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class GameMachineLoader {

	

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(GameMachineLoader.class);
	public static final Logger logger = LoggerFactory.getLogger("game_machine");
	private static ActorSystem actorSystem;

	
	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public static void StartMessageGateway() {
		actorSystem.actorOf(new RoundRobinPool(10).props(Props.create(MessageGateway.class)), 
			    MessageGateway.name);
	}
	
	public static void StartEntityTracking() {
		actorSystem.actorOf(new RoundRobinPool(10).props(Props.create(EntityTracking.class)), 
			    EntityTracking.name);
	}
	
	public void run(ActorSystem newActorSystem) {
		Thread.currentThread().setName("game-machine");
		actorSystem = newActorSystem;
		actorSystem.actorOf(Props.create(EventStreamHandler.class), EventStreamHandler.class.getSimpleName());
		
		actorSystem.actorOf(new RoundRobinPool(10).props(Props.create(MessagePersister.class)), 
			    MessagePersister.name);
	}

}
