package com.game_machine.net.server;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.Config;

public class UdpServerManager extends UntypedActor {

	public static final String CMD_START = "startServer";
	public static final String CMD_STOP = "stopServer";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public UdpServerManager() {
		if (Config.udpEnabled) {
			UdpServer.start(this.getContext().system());
		}
	}
	
	@Override
	public void postStop() {
		if (Config.udpEnabled) {
			UdpServer.stop();
		}
	}
	
	public void onReceive(Object message) {
		if (message instanceof String) {
			log.warning("Command: " + message);
			if (message.equals(CMD_START)) {
				UdpServer.stop();
			} else if (message.equals(CMD_STOP)) {
				UdpServer.start(this.getContext().system());
			} else {
				unhandled(message);
			}
			this.getSender().tell("OK", this.getSelf());
		} else {
			unhandled(message);
		}
	}

}
