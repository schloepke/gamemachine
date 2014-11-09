package com.game_machine.core;

import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ActorUtil {

	public static Object ask(String name, Object message, int timeout) {
		ActorSelection sel = getSelectionByName(name);
		Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
		AskableActorSelection askable = new AskableActorSelection(sel);
		Future<Object> future = askable.ask(message, t);
		try {
			 return Await.result(future, t.duration());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void joinCluster(String protocol, String name, String host, int port) {
		ActorSystem system = GameMachineLoader.getActorSystem();
		Address address = new Address(protocol, name, host, port);
		Cluster.get(system).join(address);
	}

	public static ActorSelection getSelectionByClass(Class<?> klass) {
		return GameMachineLoader.getActorSystem().actorSelection("/user/" + klass.getSimpleName());
	}
	
	public static ActorSelection getSelectionByName(String name) {
		return GameMachineLoader.getActorSystem().actorSelection("/user/" + name);
	}

	public static ActorSelection findLocalDistributed(String name, String id) {
		String actorName = Hashring.getHashring(name).nodeFor(id);
		return GameMachineLoader.getActorSystem().actorSelection("/user/" + actorName);
	}

	public static ActorSelection findDistributed(String name, String id) {
		String server = Hashring.getHashring("servers").nodeFor(id);
		String actorName = Hashring.getHashring(name).nodeFor(id);
		return GameMachineLoader.getActorSystem().actorSelection(server + "/user/" + actorName);
	}
	

	public static ActorSystem createSystem(String name) {
		return ActorSystem.create(name);
	}

	public static ActorSystem createSystem(String name, String hostname, String port) {
		if (hostname == null || port == null) {
			return createSystem(name);
		}

		String remoteConfig = name + ".akka.remote.netty.tcp.port=\"" + port + "\"\n" + name
				+ ".akka.remote.netty.tcp.hostname=\"" + hostname + "\"";
		Config customConfig = ConfigFactory.parseString(remoteConfig).getConfig(name)
				.withFallback(ConfigFactory.load());
		return ActorSystem.create(name, customConfig);
	}

	public static ActorSystem createSystem(String name, String remoteConfig) {
		Config customConfig = ConfigFactory.parseString(remoteConfig).getConfig(name)
				.withFallback(ConfigFactory.load());
		return ActorSystem.create(name, customConfig);
	}

}
