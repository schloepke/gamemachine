package com.game_machine.authentication;

import GameMachine.Messages.Player;

public interface Authable {
	String authtoken_for(String playerId);
	boolean isPublic();
}
