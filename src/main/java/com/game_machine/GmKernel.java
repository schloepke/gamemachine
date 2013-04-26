package com.game_machine;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.server.UdpServer;
import com.game_machine.systems.Base;

public class GmKernel {

	private static final Logger log = Logger.getLogger(GmKernel.class.getName());

	public void startup() {
		GmConfig.load();

		Base.start();
		
		if (GmConfig.udpEnabled) {
			UdpServer.logLevel = Level.parse(GmConfig.logLevel);
			UdpServer server = new UdpServer(GmConfig.udpHost, GmConfig.udpPort);

			GmContext.setUdpServer(server);
			new Thread(server).start();
		}

		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void shutdown() {
		Base.stop();
		GmContext.getUdpServer().shutdown();
	}

}
