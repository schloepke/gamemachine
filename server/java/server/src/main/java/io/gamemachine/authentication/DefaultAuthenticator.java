package io.gamemachine.authentication;

import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Player;

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
