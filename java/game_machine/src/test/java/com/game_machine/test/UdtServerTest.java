package com.game_machine.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.game_machine.ServerConfig;
import com.game_machine.InjectConfig;
import com.game_machine.actor.Pi;
import com.game_machine.client.Client;
import com.game_machine.client.UdpClient;
import com.game_machine.server.UdpServer;
import com.game_machine.server.UdtServer;
import com.game_machine.server.UdtServerHandler;
import com.game_machine.systems.Root;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class UdtServerTest {

	public static final Logger log = Logger.getLogger(UdtServerTest.class.getName());
	public static String hostname = "192.168.1.3";

	//@Test
	public void runServer() throws Exception {
		UdtServer.logLevel = Level.INFO;
		Client.logLevel = Level.INFO;
		final UdtServerHandler handler = new UdtServerHandler();
		new UdtServer(hostname, 1234).start(handler);
		Client client = new Client(hostname, 1234);
		client.run();

	}

	//@Test
	public void injectTest() {
		try {
			Injector injector = Guice.createInjector(new InjectConfig());
			Root.start(hostname, "1234");
			Thread.sleep(2000);
			Root.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void actorsystem()  {
		try {
			Root.start(hostname, "1234");
			Thread.sleep(2000);
			Root.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//@Test
	public void runUdp() throws Exception {
		
		
		Root.start(hostname, "1234");
		
		UdpServer.logLevel = Level.INFO;
		UdpClient.logLevel = Level.INFO;
		UdpServer server = new UdpServer(hostname, 1234);
		server.start();
		Thread.sleep(50000);
		server.stop();
		Root.stop();
		// UdpClient client = new UdpClient(hostname, 1234);
		// client.start();

	}

	// @Test
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
