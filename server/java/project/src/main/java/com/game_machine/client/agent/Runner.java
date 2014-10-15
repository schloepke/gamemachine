package com.game_machine.client.agent;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.game_machine.client.Api;

public class Runner implements Bootable {
	
	private static ActorSystem system;

	public static ActorSystem getActorSystem() {
		return system;
	}

	public void startup() {
		Config conf = Config.getInstance();
		system = ActorSystem.create(conf.getGameId());
		Api.setActorSystem(system);

		system.actorOf(Props.create(AgentUpdater.class), AgentUpdater.class.getSimpleName());
	}

	public void shutdown() {
		system.shutdown();
	}
}