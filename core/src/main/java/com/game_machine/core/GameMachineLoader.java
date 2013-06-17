package com.game_machine.core;

import java.util.logging.Logger;

import akka.actor.ActorSystem;
import akka.actor.Props;

import com.game_machine.core.persistence.ObjectDb;

public class GameMachineLoader {

	private static final Logger log = Logger.getLogger(GameMachineLoader.class.getName());
	private static ActorSystem actorSystem;
	private static String gameHandler;
	
	

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public static String getGameHandler() {
		return gameHandler;
	}

	public void run_test(String gameHandler) {
		actorSystem = ActorUtil.createSystem("system");
		this.gameHandler = gameHandler;
		log.info("GameMachineLoader started in test mode");
	}
	
	public void run(String name, String config, String gameHandler) {
		this.gameHandler = gameHandler;
		Thread.currentThread().setName("game-machine");
		
		if (name.equals("test")) {
			actorSystem = ActorUtil.createSystem("system");
		} else {
			actorSystem = ActorUtil.createSystem(name,config);
		}
		
		
		// Memory database actor, needs to be pinned to a single thread
		//actorSystem.actorOf(Props.create(ObjectDb.class).withDispatcher("db-dispatcher"), ObjectDb.class.getSimpleName());

		// Uility actor to send and receive commands from outside akka
		//actorSystem.actorOf(Props.create(Cmd.class), Cmd.class.getSimpleName());

		
		//actorSystem.actorOf(Props.create(Gateway.class), Gateway.class.getSimpleName());
		
		//actorSystem.actorOf(Props.create(ClientRegistry.class), ClientRegistry.class.getSimpleName());

		// Game logic entry point
		/*try {
			gameHandler = Class.forName(Config.gameHandler);
			if (Config.gameHandlerRouter.equals("round-robin")) {
				actorSystem.actorOf(Props.create(gameHandler).withRouter(new RoundRobinRouter(10)), gameHandler.getSimpleName());
			} else {
				actorSystem.actorOf(Props.create(gameHandler), gameHandler.getSimpleName());
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
		
	}

}
