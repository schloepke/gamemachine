package io.gamemachine.unity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.messages.GameMessage;

public class UnityRpcTest extends GameMessageActor {

	private static final Logger log = LoggerFactory.getLogger(UnityRpcTest.class);
	@Override
	public void awake() {
		scheduleOnce(1000l, "tick");
		
	}

	@Override
	public void onTick(String message) {
		String actorName = "ExampleActor";
		long startTime = System.nanoTime();
		for (int i=0; i<1;i++) {
			GameMessage gameMessage = new GameMessage();
			//GameMessage response = callRpc("GameMachine.ServerClient.RpcHandler.TestMethod", gameMessage);
			if (UnityGameMessageHandler.hasActor(actorName)) {
				GameMessage response = PlayerMessage.askUnity(gameMessage, actorName);
			}
			
		}
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
		//log.warn("Time "+duration);
		scheduleOnce(100l, "tick");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}

}
