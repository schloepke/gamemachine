package io.gamemachine.game_systems;

import io.gamemachine.core.Commands;
import io.gamemachine.core.DynamicMessageUtil;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import user.messages.TestMessage;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LatencyTest extends GameMessageActor {

	public static String name = LatencyTest.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public void awake() {
		Commands.clientManagerRegister(name);
	}

	public void onGameMessage(GameMessage gameMessage) {
		TestMessage testMessage = DynamicMessageUtil.fromDynamicMessage(gameMessage.getDynamicMessage());
		long latency = System.currentTimeMillis() - testMessage.sentAt;
		if (latency >= 4) {
			//logger.info("latency " + latency);
		}
		gameMessage.setAgentId(testMessage.senderId);
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	private void onPlayerDisconnect(String playerId) {

	}
}
