package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.game_machine.GameMachine;
import com.game_machine.net.client.Client;
import com.game_machine.net.server.UdtServer;

public class UdtServerTest {

	public static final Logger log = Logger.getLogger(UdtServerTest.class.getName());
	
	// @Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.INFO;
		Client.logLevel = Level.INFO;
		//final UdtServerHandler handler = new UdtServerHandler();
		//new UdtServer(hostname, 1234).start(handler);
		//Client client = new Client(hostname, 1234);
		//client.run();

	}

	@Test
	public void runUdp() {
		try {
			GameMachine.start();
			Thread.sleep(50000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//@Test
	public void writeBehindTest() {
		
	}

}
