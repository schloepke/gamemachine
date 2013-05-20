package com.game_machine.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.game.GameCommandRouter;

public class GameCommandsTest {

	//ArrayList<GameCommand> gameCommands = null;
	public static ActorSystem system;

	@BeforeClass
	public void setup() {
		system = ActorUtil.createSystem("system");
	}

	@AfterClass
	public void teardown() {
		system.shutdown();
	}

	public GameCommandRouter getHandler(String name) {
		Props props = Props.create(GameCommandRouter.class);
		TestActorRef<GameCommandRouter> ref = TestActorRef.create(system, props, name);
		return ref.underlyingActor();
	}
	
	@Test
	public void gameCommands() {
		//gameCommands = MessageUtil.loadGameCommands();
		//ClientMessage message = MessageBuilder.encode(gameCommands.get("test1").toByteArray(), "player");
		//GameCommandRouter router = getHandler("router1");
		//router.onReceive(message);
	}
}
