package com.game_machine;

import java.util.logging.Logger;

import com.game_machine.systems.Base;

public class GmKernel {

	private static final Logger log = Logger.getLogger(GmKernel.class.getName());

	public void startup() {
		GmConfig.load();

		Base.start();
		
		if (GmConfig.udpEnabled) {
			// String msg = Cmd.startUdpServer();
		}

		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void shutdown() {
		Base.shutdown();
		GmContext.getUdpServer().shutdown();
	}

}
