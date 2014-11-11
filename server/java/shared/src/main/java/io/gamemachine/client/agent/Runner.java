package io.gamemachine.client.agent;

import io.gamemachine.client.api.Api;
import io.gamemachine.codeblocks.CodeblockSecurityManager;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

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

		system.actorOf(Props.create(AgentUpdateActor.class), AgentUpdateActor.class.getSimpleName());
	}

	public void shutdown() {
		ActorSelection sel = Api.getActorByName(AgentUpdateActor.class.getSimpleName());
		String message = "disconnected:"+conf.getPlayerId();
		sel.tell(message, null);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		system.shutdown();
	}
}