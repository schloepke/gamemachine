package com.game_machine.authentication;

import org.mindrot.jbcrypt.BCrypt;

import GameMachine.Messages.Player;

public class DefaultAuthenticator implements PlayerAuthenticator {

	private static String scope = "players";
	public Player player;

	public DefaultAuthenticator(Player player) {
		this.player = player;
	}

	public void setPassword(String password) {
		player.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
		Player.store().set(scope,player);
	}

	public boolean authenticate(String password) {
		if (player.hasPasswordHash()) {
			return BCrypt.checkpw(password, player.getPasswordHash());
		} else {
			return false;
		}
	}
}
