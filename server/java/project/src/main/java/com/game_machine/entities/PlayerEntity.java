package com.game_machine.entities;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.Player;

public class PlayerEntity {

	private static final Logger logger = LoggerFactory.getLogger(PlayerEntity.class);
	private static String scope = "players";
	public Player player;
	
	public PlayerEntity(Player player) {
		this.player = player;
	}
	
	public void save() {
		player.storeSet(scope);
	}
	
	public void setPassword(String password) {
		player.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
	}
	
	public boolean passwordIsValid(String password) {
		return BCrypt.checkpw(password, player.getPasswordHash());
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
		PlayerEntity playerEntity = find(username,2000);
		if (playerEntity != null) {
			throw new RuntimeException("Player "+username+" exists");
		}
		
		Player player = new Player();
		player.setId(username);
		playerEntity = new PlayerEntity(player);
		playerEntity.setPassword(password);
		playerEntity.save();
		return playerEntity;
	}
}
