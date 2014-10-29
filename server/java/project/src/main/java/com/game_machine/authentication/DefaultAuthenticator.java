package com.game_machine.authentication;

import GameMachine.Messages.Player;

import com.game_machine.core.PlayerService;

public class DefaultAuthenticator implements PlayerAuthenticator {

	private Player player;

	public DefaultAuthenticator(Player player) {
		this.player = player;
	}

	public void setPassword(String password) {
		PlayerService.getInstance().setPassword(player.id, password);
	}

	public boolean authenticate(String password) {
		return PlayerService.getInstance().authenticate(player.id, password);
	}
}
