package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.Properties;
import java.util.logging.Level;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.game_machine.actor.Pi;
import com.game_machine.actor.RemoteTest;
import com.game_machine.udt_server.Client;
import com.game_machine.udt_server.Server;

public class UdtServerTest {

	@Test
	public void runServer() throws Exception {
		Server.logLevel = Level.WARNING;
		Client.logLevel = Level.WARNING;
		//new Server(1234).run();
		//Client client = new Client("localhost", 1234);
		//client.run();

	}

	@Test
	public void actor1() {
		try {
			//Properties systemProperties = System.getProperties();
			// systemProperties.setProperty("akka.log-config-on-start", "on");
			//Pi pi = new Pi();
			//pi.calculate(4, 10000, 10000);
			RemoteTest.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
