package com.game_machine.akka;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorUtil {

	
	public static ActorSystem createSystem(String name) {
		return ActorSystem.create(name);
	}
	
	public static ActorSystem createSystem(String name, String hostname, String port) {
		if (hostname == null || port == null) {
			return createSystem(name);
		}
		
		String remoteConfig = name + ".akka.remote.netty.port=\"" + port + "\"\n" + name + ".akka.remote.netty.hostname=\"" + hostname + "\"";
		Config customConfig = ConfigFactory.parseString(remoteConfig).getConfig(name).withFallback(ConfigFactory.load());
		return ActorSystem.create(name, customConfig);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ActorRef createActor(ActorSystem system, Class klass, String name) {
		return system.actorOf(new Props(klass), name);
	}
	
}
