package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.game_machine.ActorUtil;
import com.game_machine.Config;
import com.game_machine.GameMachine;
import com.game_machine.net.client.UdtClient;
import com.game_machine.net.client.ClientCallable;
import com.game_machine.net.client.UdpClient;
import com.game_machine.net.server.UdtServer;

public class ServerTest {

	public static final Logger log = Logger.getLogger(ServerTest.class.getName());

	@BeforeClass
	public void setup() {
		GameMachine.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public void teardown() {
		GameMachine.stop();
	}

	// @Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.INFO;
		UdtClient.logLevel = Level.INFO;
	}

	@Test
	public void runUdp() {
		final UdpClient client = new UdpClient(Config.udpHost, Config.udpPort);

		client.setCallback(new ClientCallable() {
			public void send(byte[] bytes) {
				log.warning("UdpClient GOT: " + new String(bytes));
				client.send("TEST".getBytes());
				client.stop();
			}
		});
		client.start();

	}

	@Test
	public void runUdt() {
		final UdtClient client = new UdtClient(Config.udtHost, Config.udtPort);

		client.setCallback(new ClientCallable() {
			public void send(byte[] bytes) {
				String message = new String(bytes);
				log.warning("UdtClient GOT: " + message);
				if (message.equals("READY")) {
					client.send("STOP".getBytes());
				}
				if (message.equals("STOP")) {
					client.stop();
				}
			}
		});
		client.start();
	}

}
