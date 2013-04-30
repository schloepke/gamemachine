package com.game_machine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import com.game_machine.core.actors.Inbound;
import com.game_machine.core.actors.Outbound;
import com.game_machine.core.actors.UdpServerManager;
import com.game_machine.game.actors.Echo;
import com.game_machine.persistence.Db;

public class GameMachine implements Runnable {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GameMachine.class.getName());
	private static ActorSystem actorSystem;
	private static ConcurrentHashMap<String, ActorRef> actorRefs = new ConcurrentHashMap<String, ActorRef>();
	

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Runnable worker = new GameMachine();
		executor.execute(worker);
		executor.shutdown();
		
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
		actorSystem.actorOf(Props.create(Db.class).withDispatcher("db-dispatcher"), Db.class.getSimpleName());

		// Manage the udp server
		actorSystem.actorOf(Props.create(UdpServerManager.class), UdpServerManager.class.getSimpleName());

		// Uility actor to send and receive commands from outside akka
		actorSystem.actorOf(Props.create(Cmd.class), Cmd.class.getSimpleName());

		// For testing
		actorSystem.actorOf(Props.create(Echo.class), Echo.class.getSimpleName());

		// Main incoming/outgoing channels between game client and game logic
		// actors
		actorSystem.actorOf(Props.create(Inbound.class).withRouter(new RoundRobinRouter(10)), Inbound.class.getSimpleName());
		actorSystem.actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), Outbound.class.getSimpleName());

	}

}
