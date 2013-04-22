package com.game_machine.systems;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.agent.Agent;

import com.game_machine.server.UdpServer;

public class Root {

	public static ActorSystem system;

	public static Boolean isRunning() {
		if (Root.system == null || Root.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}

	public static void start(String hostname, String port) {
		Root.system = ActorUtil.createSystem("system", hostname, port);
		
		Root.system.actorOf(new Props(Outbound.class), "outbound");
		Root.system.actorOf(new Props(Inbound.class), "inbound");
		
		
		//Agent<UdpServer> agent = new Agent<UdpServer>(null, Root.system);
		
		//ActorSelection ref = Root.system.actorSelection("/user/outbound");
	}

	public static void stop() {
		Root.system.shutdown();
	}
}
