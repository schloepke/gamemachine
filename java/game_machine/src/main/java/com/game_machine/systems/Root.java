package com.game_machine.systems;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import akka.routing.RoundRobinRouter;

public class Root {

	public static ActorSystem system;
	public static ActorRef router;
	
	public static Boolean isRunning() {
		if (Root.system == null || Root.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}

	public static void start(String hostname, String port) {
		Root.system = ActorUtil.createSystem("system", hostname, port);
		ActorUtil.createSystem("app1", "192.168.1.3", "2553");
		//Root.system = ActorUtil.createSystem("system", null, null);
		
		//Root.system.actorOf(Props.create(Outbound.class), "outbound");
		//Root.system.actorOf(Props.create(Inbound.class), "inbound");
		
		Root.system.actorOf(Props.create(Inbound.class).withRouter(new RoundRobinRouter(10)), "inbound");
		Root.system.actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), "outbound");
	}

	
	
	public static void stop() {
		Root.system.shutdown();
	}
}
