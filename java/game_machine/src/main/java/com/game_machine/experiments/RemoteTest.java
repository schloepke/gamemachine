package com.game_machine.experiments;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class RemoteTest extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void onReceive(Object message) throws Exception {
		if (message instanceof String)
			log.info("Received String message: {}", message);
		else
			unhandled(message);
	}

	public static void test() {

		// ConfigFactory.load();
		// System.out.println(ConfigFactory.systemEnvironment());
		// System.out.println(ConfigFactory.systemProperties());
		// Config config = ConfigFactory.load();
		// c.getConfig("myapp1");
		// System.out.println(c.toString());
		// appConfig.withFallback(ConfigFactory.defaultReference(classLoader))
		// ConfigFactory.parseString("akka.remote.netty.port=\"1.2.3.4\"").withFallback(ConfigFactory.load());

		// ActorSystem app1 =
		// ActorSystem.create("app1",config.getConfig("app1").withFallback(config));
		// ActorSystem app2 =
		// ActorSystem.create("app2",config.getConfig("app2").withFallback(config));

		String port;
		String app;
		String portConfig;
		Config customConfig;
		ArrayList<ActorSystem> systems = new ArrayList<ActorSystem>();
		ActorSystem system;
		ActorRef ref;
		for (int i = 1; i < 11; i++) {
			port = Integer.toString(2552 + i);
			app = "app" + i;
			portConfig = app + ".akka.remote.netty.port=\"" + port + "\"";
			System.out.println("Port " + portConfig);
			customConfig = ConfigFactory.parseString(portConfig).getConfig(app).withFallback(ConfigFactory.load());
			system = ActorSystem.create(app, customConfig);
			systems.add(system);

			ref = system.actorOf(new Props(RemoteTest.class), "myactor");
			ref.tell("HELLO",ref);
		}
	}
}