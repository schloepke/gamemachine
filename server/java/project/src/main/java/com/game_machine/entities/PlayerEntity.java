package com.game_machine.entities;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.Player;
import GameMachine.Messages.PlayerCredentials;

public class PlayerEntity {

	private static final Logger logger = LoggerFactory.getLogger(PlayerEntity.class);
	private static String scope = "players";
	public Player player;
	private PlayerCredentials playerCredentials;

	public PlayerEntity(Player player) {
		this.player = player;
		playerCredentials = PlayerCredentials.storeGet("player_credentials", this.player.id, 1000);
		if (playerCredentials == null) {
			playerCredentials = new PlayerCredentials().setId(this.player.id);
			playerCredentials.storeSet("player_credentials");
		}

	}

	public void save() {
		player.storeSet(scope);
		playerCredentials.storeSet("player_credentials");
	}

	public void setPassword(String password) {
		playerCredentials.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
	}

	public boolean passwordIsValid(String password) {
		if (playerCredentials.hasPasswordHash()) {
			return BCrypt.checkpw(password, playerCredentials.getPasswordHash());
		} else {
			return false;
		}
	}

	public static PlayerEntity find(String id, int timeout) {
		Player player = Player.storeGet(scope, id, timeout);
		if (player == null) {
			return null;
		} else {
			return new PlayerEntity(player);
		}
	}

	public static PlayerEntity create(String username, String password) {
		PlayerEntity playerEntity = find(username, 2000);
		if (playerEntity != null) {
			throw new RuntimeException("Player " + username + " exists");
		}

		Player player = new Player();
		player.setId(username);
		playerEntity = new PlayerEntity(player);
		playerEntity.setPassword(password);
		playerEntity.save();
		return playerEntity;
	}
}
