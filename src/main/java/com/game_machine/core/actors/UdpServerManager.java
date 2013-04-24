package com.game_machine.core.actors;

import java.util.logging.Level;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GmConfig;
import com.game_machine.GmContext;
import com.game_machine.core.net.UdpServer;

public class UdpServerManager extends UntypedActor {

	public static final String CMD_START = "startServer";
	public static final String CMD_STOP = "stopServer";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public UdpServerManager() {
		if (GmConfig.udpEnabled) {
			this.getSelf().tell(CMD_START,null);
		}
	}
	
	public void onReceive(Object message) {
		if (message instanceof String) {
			log.warning("Command: " + message);
			if (message.equals(CMD_START)) {
				startServer();
			} else if (message.equals(CMD_STOP)) {
				stopServer();
			} else {
				unhandled(message);
			}
			this.getSender().tell("OK", this.getSelf());
		} else {
			unhandled(message);
		}

	}

	public void startServer() {
		UdpServer.logLevel = Level.parse(GmConfig.logLevel);
		UdpServer server = new UdpServer(GmConfig.udpHost, GmConfig.udpPort);
		new Thread(server).start();
		GmContext.setUdpServer(server);
	}

	public void stopServer() {
		GmContext.getUdpServer().shutdown();
		GmContext.setUdpServer(null);
	}
}
