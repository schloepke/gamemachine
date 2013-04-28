package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.game_machine.GameMachine;
import com.game_machine.InjectConfig;
import com.game_machine.core.net.Client;
import com.game_machine.core.net.UdtServer;
import com.game_machine.core.net.UdtServerHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class UdtServerTest {

	public static final Logger log = Logger.getLogger(UdtServerTest.class.getName());
	
	// @Test
	public void runServer() throws Exception {
		Injector injector = Guice.createInjector(new InjectConfig());
		UdtServer.logLevel = Level.INFO;
		Client.logLevel = Level.INFO;
		final UdtServerHandler handler = new UdtServerHandler();
		//new UdtServer(hostname, 1234).start(handler);
		//Client client = new Client(hostname, 1234);
		//client.run();

	}

	@Test
	public void runUdp() {
		try {
			GameMachine kernel = new GameMachine();
			kernel.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
