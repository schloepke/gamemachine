package com.game_machine.server;

import com.game_machine.actor.Pi.Listener;
import com.game_machine.akka.ActorUtil;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Router extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	public static ActorSystem system;
	public static ActorRef router;
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String)
			log.info("Received String message: {}", message);
		else
			unhandled(message);
	}
	
	public static void start(String name, String hostname, String port) {
		Router.system = ActorUtil.createSystem(name, hostname, port);
		Router.router = ActorUtil.createActor(Router.system, Router.class, name+"_router");
	}
	
	public static void stop() {
		Router.system.shutdown();
	}
}
