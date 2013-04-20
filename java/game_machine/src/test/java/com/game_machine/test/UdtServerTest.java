package com.game_machine.test;

import java.util.logging.Level;

import org.testng.annotations.Test;

import com.game_machine.actor.Pi;
import com.game_machine.client.Client;
import com.game_machine.client.UdpClient;
import com.game_machine.server.GameProtocolServer;
import com.game_machine.server.Router;
import com.game_machine.server.UdpServer;
import com.game_machine.server.UdpServerHandler;
import com.game_machine.server.UdtServer;
import com.game_machine.server.UdtServerHandler;

public class UdtServerTest {

	public static String hostname = "192.168.1.3";

	// @Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.WARNING;
		Client.logLevel = Level.WARNING;
		final UdtServerHandler handler = new UdtServerHandler();
		new UdtServer(hostname, 1234).start(handler);
		Client client = new Client(hostname, 1234);
		client.run();

	}

	@Test
	public void runUdp() throws Exception {
		Router.start(hostname,"1234");
		
		UdpServer.logLevel = Level.INFO;
		UdpClient.logLevel = Level.INFO;
		GameProtocolServer server = new UdpServer(hostname, 1234);
		server.start();
		Thread.sleep(5000);
		server.stop();
		Router.stop();
		//UdpClient client = new UdpClient(hostname, 1234);
		//client.start();

	}

	//@Test
	public void actor1() {
		try {
			// Properties systemProperties = System.getProperties();
			// systemProperties.setProperty("akka.log-config-on-start", "on");
			 Pi pi = new Pi();
			 pi.calculate(4, 10000, 10000);
			// RemoteTest.test();
			// Router.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
