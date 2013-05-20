package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import com.game_machine.core.game.Echo;
import com.game_machine.core.game.Gateway;
import com.game_machine.core.net.server.UdpServer;
import com.game_machine.core.net.server.UdtServer;
import com.game_machine.core.persistence.ObjectDb;

public class GameMachine implements Runnable {

	private static final Logger log = Logger.getLogger(GameMachine.class.getName());
	private static ActorSystem actorSystem;
	private static ConcurrentHashMap<String, ActorRef> actorRefs = new ConcurrentHashMap<String, ActorRef>();
	private static Class<?> gameHandler;
	
	public static void main(String[] args) {
		start();
	}

	public static void start() {
		new GameMachine().run();
		if (Config.udpEnabled) {
			UdpServer.start();
		}
		if (Config.udtEnabled) {
			UdtServer.start();
		}
		
		// Allow time for server to start
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("GameMachine started");
	}

	public static void stop() {
		if (Config.udpEnabled) {
			UdpServer.stop();
		}
		if (Config.udtEnabled) {
			UdtServer.stop();
		}
		actorSystem.shutdown();
		log.info("GameMachine stopped");
	}
	
	public static ActorRef getActorRef(String name) {
		return actorRefs.get(name);
	}

	public static void setActorRef(String name, ActorRef ref) {
		if (ref == null) {
			actorRefs.remove(name);
		} else {
			actorRefs.put(name, ref);
		}
	}

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public static Class<?> getGameHandler() {
		return gameHandler;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("game-machine");
		
		actorSystem = ActorUtil.createSystem("system");

		// Memory database actor, needs to be pinned to a single thread
		actorSystem.actorOf(Props.create(ObjectDb.class).withDispatcher("db-dispatcher"), ObjectDb.class.getSimpleName());

		// Uility actor to send and receive commands from outside akka
		actorSystem.actorOf(Props.create(Cmd.class), Cmd.class.getSimpleName());

		// For testing
		actorSystem.actorOf(Props.create(Echo.class), Echo.class.getSimpleName());
		
		actorSystem.actorOf(Props.create(Gateway.class), Gateway.class.getSimpleName());

		// Game logic entry point
		try {
			gameHandler = Class.forName(Config.gameHandler);
			if (Config.gameHandlerRouter.equals("round-robin")) {
				actorSystem.actorOf(Props.create(gameHandler).withRouter(new RoundRobinRouter(10)), gameHandler.getSimpleName());
			} else {
				actorSystem.actorOf(Props.create(gameHandler), gameHandler.getSimpleName());
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
