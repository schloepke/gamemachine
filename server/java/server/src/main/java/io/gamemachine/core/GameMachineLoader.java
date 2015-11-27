package io.gamemachine.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GameLimits;
import io.gamemachine.grid.GridExpiration;
import io.gamemachine.messages.GameConfig;
import io.gamemachine.objectdb.DbActor;
import io.gamemachine.process.ProcessManager;
import io.gamemachine.routing.GameMessageRoute;
import io.gamemachine.routing.Incoming;
import io.gamemachine.routing.RequestHandler;
import io.gamemachine.routing.UnityGameMessageHandler;
import io.gamemachine.routing.UnityRpcTest;
import io.gamemachine.zones.ZoneManager;

public class GameMachineLoader {

	private static final Logger log = LoggerFactory.getLogger(GameMachineLoader.class);
	public static final Logger logger = LoggerFactory.getLogger("game_machine");
	private static ActorSystem actorSystem;

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}

	public static void preStart() {
		
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
		PluginManager.startAll();
	}

	public void run(ActorSystem newActorSystem) {
		Thread.currentThread().setName("game-machine");
		
		actorSystem = newActorSystem;
				
		actorSystem.actorOf(Props.create(UnityGameMessageHandler.class), UnityGameMessageHandler.name);
		actorSystem.actorOf(Props.create(UnityRpcTest.class), UnityRpcTest.class.getSimpleName());
		
		actorSystem.actorOf(Props.create(EventStreamHandler.class), EventStreamHandler.class.getSimpleName());
		actorSystem.actorOf(new RoundRobinPool(20).props(Props.create(RemoteEcho.class)), RemoteEcho.name);
	
		startCacheUpdateHandler();
		actorSystem.actorOf(Props.create(GameLimits.class), GameLimits.class.getSimpleName());
		
		GameMessageRoute.add(ProcessManager.name, ProcessManager.name, false);
		ActorUtil.createActor(ProcessManager.class, ProcessManager.name);
		
		if (AppConfig.Datastore.getStore().equals("gamecloud")) {
			actorSystem.actorOf(Props.create(GameConfig.class), GameConfig.class.getSimpleName());
		}
		
		if (AppConfig.getOrm()) {
			ZoneManager.start();
		} else {
			logger.warn("Zone manager disabled (requires orm=true)");
		}
		
		actorSystem.actorOf(Props.create(GridExpiration.class), GridExpiration.class.getSimpleName());
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ProcessManager.stopAll();
                getActorSystem().shutdown();
            }
        });

	}

}
