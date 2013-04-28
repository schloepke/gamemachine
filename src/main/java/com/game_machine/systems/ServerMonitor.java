package com.game_machine.systems;

import java.lang.reflect.Method;
import java.util.logging.Level;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GmConfig;
import com.game_machine.GmContext;
import com.game_machine.messages.CmdMessage;
import com.game_machine.server.UdpServer;

public class ServerMonitor extends UntypedActor {

	public static final String CMD_START_UDP_SERVER = "startUdpServer";
	public static final String CMD_STOP_UDP_SERVER = "stopUdpServer";
	public static final String CMD_START_UDT_SERVER = "startUdtServer";
	public static final String CMD_STOP_UDT_SERVER = "stopUdtServer";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void onReceive(Object message) {
		if (message instanceof CmdMessage) {
			CmdMessage cmdMessage = (CmdMessage) message;
			log.warning("Command: " + cmdMessage.getCmd());
			try {
				Method method = this.getClass().getMethod(cmdMessage.getCmd(), null);
				method.invoke(this, null);
			} catch (Exception e) {
				e.printStackTrace();
				this.getSender().tell(cmdMessage.setResult("OK").setError(true), this.getSelf());
			}
			this.getSender().tell(cmdMessage.setResult("OK"), this.getSelf());
		} else {
			unhandled(message);
		}

	}

	public void startUdpServer() {
		UdpServer.logLevel = Level.parse(GmConfig.logLevel);
		UdpServer server = new UdpServer(GmConfig.udpHost, GmConfig.udpPort);
		new Thread(server).start();
		GmContext.setUdpServer(server);
	}

	public void stopUdpServer() {
		GmContext.getUdpServer().shutdown();
		GmContext.setUdpServer(null);
	}
}
