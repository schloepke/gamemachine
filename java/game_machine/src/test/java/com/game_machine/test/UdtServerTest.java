package com.game_machine.test;

import java.util.logging.Level;

import org.testng.annotations.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import com.game_machine.actor.RemoteTest;
import com.game_machine.akka.ActorUtil;
import com.game_machine.client.Client;
import com.game_machine.server.Router;
import com.game_machine.server.Server;

public class UdtServerTest {

	@Test
	public void runServer() throws Exception {
		Server.logLevel = Level.WARNING;
		Client.logLevel = Level.WARNING;
		//new Server("localhost",1234).run();
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
			//RemoteTest.test();
			//Router.start();
			ActorSystem system = ActorUtil.createSystem("test", "localhost", "3333");
			ActorRef ref = ActorUtil.createActor(system, Router.class, "router_name");
			ref.tell("HELLO", ref);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
