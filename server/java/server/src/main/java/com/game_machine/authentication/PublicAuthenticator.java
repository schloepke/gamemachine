package com.game_machine.authentication;

import GameMachine.Messages.Player;

public class PublicAuthenticator implements PlayerAuthenticator {

	public PublicAuthenticator(Player player) {
		
	}
	
	@Override
	public void setPassword(String password) {
	}

	@Override
	public boolean authenticate(String password) {
		return true;
	}

}
