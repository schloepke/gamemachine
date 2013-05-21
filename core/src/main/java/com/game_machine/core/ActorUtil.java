package com.game_machine.core;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ActorUtil {

	
	public static ActorSelection getSelectionByClass(Class<?> klass) {
		return GameMachine.getActorSystem().actorSelection("/user/"+ klass.getSimpleName());
	}
	
	public static ActorSelection getSelectionByName(String name) {
		return GameMachine.getActorSystem().actorSelection("/user/"+ name);
	}
	
	public static ActorRef getActorByClass(Class<?> klass) {
		return GameMachine.getActorRef(klass.getSimpleName());
	}
	
	public static ActorSystem createSystem(String name) {
		return ActorSystem.create(name);
	}
	
	public static ActorSystem createSystem(String name, String hostname, String port) {
		if (hostname == null || port == null) {
			return createSystem(name);
		}
		
		String remoteConfig = name + ".akka.remote.netty.tcp.port=\"" + port + "\"\n" + name + ".akka.remote.netty.tcp.hostname=\"" + hostname + "\"";
		Config customConfig = ConfigFactory.parseString(remoteConfig).getConfig(name).withFallback(ConfigFactory.load());
		return ActorSystem.create(name, customConfig);
	}
			
}
