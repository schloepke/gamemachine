package io.gamemachine.unity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;

public class UnityInstanceTest extends GameMessageActor {

	public static AtomicInteger messageCount = new AtomicInteger();
	private static final Logger log = LoggerFactory.getLogger(UnityInstanceTest.class);
	
	private List<String> names = new ArrayList<String>();
	
	@Override
	public void awake() {
		for (int i = 0; i < 5; i++) {
			String name = "testInstance" + i;
			names.add(name);
			GameMachineLoader.getActorSystem().actorOf(Props.create(TestInstance.class, name));
		}
		
		scheduleOnce(1000l, "tick");
	}

	@Override
	public void onTick(String message) {
		int count = messageCount.get();
		messageCount = new AtomicInteger();
		log.warn("mps "+ count);
		scheduleOnce(1000l, "tick");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}

}
