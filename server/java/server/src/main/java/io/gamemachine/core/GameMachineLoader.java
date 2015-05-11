package io.gamemachine.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GameConfig;
import io.gamemachine.config.GameLimits;
import io.gamemachine.game_systems.LatencyTest;
import io.gamemachine.game_systems.PathService;
import io.gamemachine.objectdb.DbActor;
import io.gamemachine.routing.Incoming;
import io.gamemachine.routing.RequestHandler;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvp_game.GameLoader;
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

	// TODO move router count to a config
	public static void StartEntityTracking() {
		actorSystem.actorOf(new RoundRobinPool(30).props(Props.create(EntityTracking.class)), EntityTracking.name);
	}

	public static void startObjectDb(int nodeCount) {

		ArrayList<String> nodes = new ArrayList<String>();
		for (int i = 1; i < nodeCount; i++) {
			nodes.add("object_store" + i);
		}
		Hashring ring = new Hashring("object_store", nodes, 3);
		for (String node : ring.nodes) {
			actorSystem.actorOf(Props.create(DbActor.class), node);
		}
	}

	public static void startRequestHandler(int nodeCount) {
		actorSystem.actorOf(new RoundRobinPool(nodeCount).props(Props.create(RequestHandler.class)),
				RequestHandler.name);
	}

	public static void startIncoming(int nodeCount) {
		actorSystem.actorOf(new RoundRobinPool(nodeCount).props(Props.create(Incoming.class)), Incoming.name);
	}

	public static void startCacheUpdateHandler() {
		ArrayList<String> nodes = new ArrayList<String>();
		for (int i = 1; i < 50; i++) {
			nodes.add("node" + i);
		}
		Hashring ring = new Hashring("cacheUpdateHandler", nodes, 3);
		for (String node : ring.nodes) {
			actorSystem.actorOf(Props.create(CacheUpdateHandler.class), node);
		}
	}

	public static void startJavaGameActors() {
		actorSystem.actorOf(Props.create(UnityProxy.class), UnityProxy.name);
		GameLoader.load();
	}

	public void run(ActorSystem newActorSystem) {
		Thread.currentThread().setName("game-machine");
		actorSystem = newActorSystem;
		actorSystem.actorOf(Props.create(EventStreamHandler.class), EventStreamHandler.class.getSimpleName());
		actorSystem.actorOf(new RoundRobinPool(20).props(Props.create(RemoteEcho.class)), RemoteEcho.name);
		actorSystem.actorOf(new RoundRobinPool(10).props(Props.create(LatencyTest.class)), LatencyTest.name);
		// actorSystem.actorOf(Props.create(PathService.class),
		// PathService.name);
		
		startCacheUpdateHandler();

		if (AppConfig.Datastore.getStore().equals("gamecloud")) {
			actorSystem.actorOf(Props.create(GameConfig.class), GameConfig.class.getSimpleName());
			actorSystem.actorOf(Props.create(GameLimits.class), GameLimits.class.getSimpleName());
		}

	}

}
