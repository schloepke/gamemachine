package com.game_machine.server;

import io.netty.channel.Channel;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.akka.ActorUtil;
import com.game_machine.messages.ProtobufMessages.ClientMsg;

public class Router extends UntypedActor {

	public static ActorSystem system = null;
	public static ActorRef router = null;
	private GameProtocolServer server = null;
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Router() {
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			log.info("ROUTER.INCOMING String message: {}", message);
		} else if (message instanceof ClientMsg) {
			log.info("Router ClientMsg");
			ClientMsg msg = (ClientMsg) message;
			
			// echo it back for now
			server.sendMessage(msg.getBody().toStringUtf8(), msg.getHostname(), server.getPort());
		} else if (message instanceof GameProtocolServer) {
			log.info("Router GameProtocolServer");
			server = (GameProtocolServer) message;
		} else {
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
		Router.system = ActorUtil.createSystem("routerSystem",hostname, port);
		
		Router.router = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Router();
			}
		}), "router");
	}

	public static void stop() {
		Router.system.shutdown();
	}
}
