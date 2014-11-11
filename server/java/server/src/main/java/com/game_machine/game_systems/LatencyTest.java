package com.game_machine.game_systems;

import user.messages.TestMessage;
import GameMachine.Messages.GameMessage;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.core.Commands;
import com.game_machine.core.DynamicMessageUtil;
import com.game_machine.core.GameMessageActor;
import com.game_machine.core.PlayerCommands;

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
			logger.info("latency " + latency);
		}
		gameMessage.setAgentId(testMessage.senderId);
		PlayerCommands.sendGameMessage(gameMessage, playerId);
	}

	private void onPlayerDisconnect(String playerId) {

	}
}
