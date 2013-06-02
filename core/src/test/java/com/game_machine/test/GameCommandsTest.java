package com.game_machine.test;

import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.core.game.GameCommandRouter;
import com.game_machine.core.persistence.HashRing;
import com.game_machine.entity_system.Component;
import com.game_machine.entity_system.generated.ClientConnection;
import static org.fest.assertions.api.Assertions.assertThat;

public class GameCommandsTest {

	public static final Logger log = Logger.getLogger(GameCommandsTest.class.getName());
	
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
	
	//@Test
	public void gameCommands() {
		//gameCommands = MessageUtil.loadGameCommands();
		//ClientMessage message = MessageBuilder.encode(gameCommands.get("test1").toByteArray(), "player");
		//GameCommandRouter router = getHandler("router1");
		//router.onReceive(message);
	}
	
	//@Test
	public void gameMessageTest() {
		ClientConnection c = new ClientConnection();
		GameMessage message = new GameMessage("test",null,null,c);
		assertThat(message.getComponent() instanceof ClientConnection).isTrue();
		assertThat(message.getComponent() instanceof Component).isTrue();
	}
	
	//@Test
	public void hashtest() {
		HashRing ring = new HashRing("test",10);
		/*log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",9);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",8);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",7);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",6);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",5);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",4);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",3);
		log.info("BUCKET= " + ring.stringToBucket("chris"));
		ring = new HashRing("test",2);
		log.info("BUCKET= " + ring.stringToBucket("chris"));*/
		
	}
}
