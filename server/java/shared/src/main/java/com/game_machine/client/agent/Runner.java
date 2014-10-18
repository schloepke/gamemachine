package com.game_machine.client.agent;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.game_machine.client.Api;
import com.game_machine.shared.codeblocks.CodeblockSecurityManager;

public class Runner implements Bootable {
	
	private static ActorSystem system;
	private static Config conf = Config.getInstance();

	public static Config getConfig() {
		return conf;
	}
	
	public static ActorSystem getActorSystem() {
		return system;
	}

	public void startup() {
		System.setSecurityManager(new CodeblockSecurityManager());
		
		system = ActorSystem.create(conf.getGameId());
		Api.setActorSystem(system);

		system.actorOf(Props.create(AgentUpdater.class), AgentUpdater.class.getSimpleName());
	}

	public void shutdown() {
		system.shutdown();
	}
}