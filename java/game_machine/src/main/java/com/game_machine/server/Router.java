package com.game_machine.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.akka.ActorUtil;

public class Router {

	public static ActorSystem system = null;
	public static ActorRef incoming = null;
	public static ActorRef outgoing = null;

	public static class Incoming extends UntypedActor {

		LoggingAdapter log = Logging.getLogger(getContext().system(), this);

		public void onReceive(Object message) throws Exception {
			if (message instanceof String)
				log.info("ROUTER.INCOMING String message: {}", message);
			else
				unhandled(message);
		}
	}

	public static class Outgoing extends UntypedActor {

		LoggingAdapter log = Logging.getLogger(getContext().system(), this);

		public void onReceive(Object message) throws Exception {
			if (message instanceof String)
				log.info("ROUTER.OUTGOING String message: {}", message);
			else
				unhandled(message);
		}
	}

	public static Boolean isRunning() {
		if (Router.system == null || Router.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void start(String hostname, String port) {
		Router.system = ActorUtil.createSystem("router",hostname, port);
		Router.incoming = Router.system.actorOf(new Props(Incoming.class), "incoming");
		Router.outgoing = Router.system.actorOf(new Props(Outgoing.class), "outgoing");
	}

	public static void shutdown() {
		Router.system.shutdown();
	}
}
