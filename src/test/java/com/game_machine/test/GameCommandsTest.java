package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_machine.ActorUtil;
import com.game_machine.MessageBuilder;
import com.game_machine.Pb.ClientMessage;
import com.game_machine.Pb.GameCommand;
import com.game_machine.game.GameCommandRouter;

public class GameCommandsTest {

	ArrayList<GameCommand> gameCommands = null;
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
		gameCommands = MessageBuilder.loadGameCommands();
		//ClientMessage message = MessageBuilder.encode(gameCommands.get("test1").toByteArray(), "player");
		//GameCommandRouter router = getHandler("router1");
		//router.onReceive(message);
	}
}
