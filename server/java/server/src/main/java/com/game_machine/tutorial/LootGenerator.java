package com.game_machine.tutorial;

import GameMachine.Messages.GameMessage;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.core.Commands;
import com.game_machine.core.GameMessageActor;

public class LootGenerator extends GameMessageActor {
	public static String name = "tutorial_loot_generator";
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public void awake() {
		Commands.clientManagerRegister(name);
	}

	public void onGameMessage(GameMessage gameMessage) {

	}

	private void onPlayerDisconnect(String playerId) {

	}

}
