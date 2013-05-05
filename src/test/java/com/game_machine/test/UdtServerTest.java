package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.game_machine.Config;
import com.game_machine.GameMachine;
import com.game_machine.net.client.UdtClient;
import com.game_machine.net.client.ClientCallable;
import com.game_machine.net.client.UdpClient;
import com.game_machine.net.server.UdtServer;

public class UdtServerTest {

	public static final Logger log = Logger.getLogger(UdtServerTest.class.getName());
	
	// @Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.INFO;
		UdtClient.logLevel = Level.INFO;
		//final UdtServerHandler handler = new UdtServerHandler();
		//new UdtServer(hostname, 1234).start(handler);
		//Client client = new Client(hostname, 1234);
		//client.run();

	}

	@Test
	public void runUdp() {
		try {
			new GameMachine().run();
			final UdpClient client = new UdpClient(Config.udpHost,Config.udpPort);
			
			client.setCallback(new ClientCallable() {
				public void send(byte[] bytes) {
					log.warning("CLIENT GOT: " + bytes.length);
					client.stop();
					GameMachine.stop();
				}
			});
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void runUdt() {
		try {
			new GameMachine().run();
			final UdtClient client = new UdtClient(Config.udpHost,Config.udpPort);
			
			client.setCallback(new ClientCallable() {
				public void send(byte[] bytes) {
					log.warning("CLIENT GOT: " + bytes.length);
					client.stop();
					GameMachine.stop();
				}
			});
			client.start();
			//Thread.sleep(50000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
