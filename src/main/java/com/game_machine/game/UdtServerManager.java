package com.game_machine.game;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.Config;
import com.game_machine.net.server.UdtServer;

public class UdtServerManager extends UntypedActor {

	public static final String CMD_START = "startServer";
	public static final String CMD_STOP = "stopServer";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public UdtServerManager() {
		if (Config.udtEnabled) {
			UdtServer.start();
		}
	}
	
	@Override
	public void postStop() {
		if (Config.udtEnabled) {
			UdtServer.stop();
		}
	}
	
	public void onReceive(Object message) {
		if (message instanceof String) {
			log.warning("Command: " + message);
			if (message.equals(CMD_START)) {
				UdtServer.stop();
			} else if (message.equals(CMD_STOP)) {
				UdtServer.start();
			} else {
				unhandled(message);
			}
			this.getSender().tell("OK", this.getSelf());
		} else {
			unhandled(message);
		}
	}

}
