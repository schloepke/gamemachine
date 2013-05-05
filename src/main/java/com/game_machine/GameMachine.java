package com.game_machine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import com.game_machine.game.Echo;
import com.game_machine.game.Game;
import com.game_machine.game.Inbound;
import com.game_machine.game.Outbound;
import com.game_machine.net.server.UdpServer;
import com.game_machine.net.server.UdtServer;
import com.game_machine.persistence.ObjectDb;

public class GameMachine implements Runnable {

	private static final Logger log = Logger.getLogger(GameMachine.class.getName());
	private static ActorSystem actorSystem;
	private static ConcurrentHashMap<String, ActorRef> actorRefs = new ConcurrentHashMap<String, ActorRef>();

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		int udtMessageEncoding = UdtServer.ENCODING_NONE;
		int udpMessageEncoding = UdpServer.ENCODING_NONE;
		if (Config.udpEncoding.equals("pb")) {
			udpMessageEncoding = UdpServer.ENCODING_PROTOBUF;
		}
		if (Config.udtEncoding.equals("pb")) {
			udtMessageEncoding = UdtServer.ENCODING_PROTOBUF;
		}
		start(udtMessageEncoding,udpMessageEncoding);
	}
	
	
	public static void start(int udtMessageEncoding, int udpMessageEncoding) {
		new GameMachine().run();
		if (Config.udpEnabled) {
			UdpServer.start(udpMessageEncoding);
		}
		if (Config.udtEnabled) {
			UdtServer.start(udtMessageEncoding);
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

		// Main incoming/outgoing channels between game client and game logic
		// actors
		actorSystem.actorOf(Inbound.mkProps(Game.class, 10), Inbound.class.getSimpleName());
		actorSystem.actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), Outbound.class.getSimpleName());

	}

}
