package com.game_machine.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.akka.ActorUtil;
import com.game_machine.messages.ProtobufMessages.ClientMsg;
import com.game_machine.server.GameProtocolServer;

public class Master extends UntypedActor {

	public static ActorSystem system = null;
	public static ActorRef router = null;
	private GameProtocolServer server = null;
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Master() {
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			log.info("ROUTER.INCOMING String message: {}", message);
		} else if (message instanceof ClientMsg) {
			//log.info("Router ClientMsg");
			ClientMsg msg = (ClientMsg) message;
			
			// echo it back for now
			server.sendMessage(msg.getBody().toStringUtf8(), msg.getHostname(), msg.getPort());
		} else if (message instanceof GameProtocolServer) {
			log.info("Router GameProtocolServer");
			server = (GameProtocolServer) message;
		} else {
			unhandled(message);
		}
	}
	

	public static Boolean isRunning() {
		if (Master.system == null || Master.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("serial")
	public static void start(String hostname, String port) {
		Master.system = ActorUtil.createSystem("routerSystem",hostname, port);
		
		Master.router = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Master();
			}
		}), "router");
	}

	public static void stop() {
		Master.system.shutdown();
	}
}
