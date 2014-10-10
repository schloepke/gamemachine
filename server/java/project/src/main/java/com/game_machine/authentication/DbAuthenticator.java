package com.game_machine.authentication;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import GameMachine.Messages.Player;
import GameMachine.Messages.PlayerAttributes;

public class DbAuthenticator implements PlayerAuthenticator {

	private PlayerAttributes playerAttributes;
	
	public DbAuthenticator(Player player) {
		playerAttributes = PlayerAttributes.db().findFirst("player_attributes_player_id = ?",player.id);
		
		if (playerAttributes == null) {
			playerAttributes = new PlayerAttributes().setId(player.id).setGameId(player.gameId);
			PlayerAttributes.db().save(playerAttributes);
		}
	}
	
	@Override
	public void setPassword(String password) {
		playerAttributes.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
		PlayerAttributes.db().save(playerAttributes);
	}

	@Override
	public boolean authenticate(String password) {
		if (playerAttributes.hasPasswordHash()) {
			return BCrypt.checkpw(password, playerAttributes.getPasswordHash());
		} else {
			return false;
		}
	}

}
