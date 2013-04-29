package com.game_machine.core.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GmConfig;
import com.game_machine.core.net.UdpServer;

public class UdpServerManager extends UntypedActor {

	public static final String CMD_START = "startServer";
	public static final String CMD_STOP = "stopServer";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public UdpServerManager() {
		if (GmConfig.udpEnabled) {
			UdpServer.start();
		}
	}
	
	@Override
	public void postStop() {
		if (GmConfig.udpEnabled) {
			UdpServer.stop();
		}
	}
	
	public void onReceive(Object message) {
		if (message instanceof String) {
			log.warning("Command: " + message);
			if (message.equals(CMD_START)) {
				UdpServer.stop();
			} else if (message.equals(CMD_STOP)) {
				UdpServer.start();
			} else {
				unhandled(message);
			}
			this.getSender().tell("OK", this.getSelf());
		} else {
			unhandled(message);
		}
	}

}
