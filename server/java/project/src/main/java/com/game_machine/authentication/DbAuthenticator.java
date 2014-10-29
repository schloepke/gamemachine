package com.game_machine.authentication;

import GameMachine.Messages.Player;

import com.game_machine.core.PlayerService;

public class DbAuthenticator implements PlayerAuthenticator {

	private Player player;
	
	public DbAuthenticator(Player player) {
		this.player = player;
	}
	
	@Override
	public void setPassword(String password) {
		PlayerService.getInstance().setPassword(player.id, password);
	}

	@Override
	public boolean authenticate(String password) {
		return PlayerService.getInstance().authenticate(player.id, password);
	}

}
