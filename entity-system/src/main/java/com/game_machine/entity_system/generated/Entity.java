package com.game_machine.entity_system.generated;

import java.util.HashMap;

public class Entity {

	private Player player;
	private TestObject testObject;
	private PlayersAroundMe playersAroundMe;
	private GameCommand gameCommand;
	private final Integer id;
	private String clientId;
	
	public Entity(Integer id) {
		this.id = id;
	}
		
	public Integer getId() {
		return this.id;
	}
	
	public String getClientId() {
		return this.clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	// Player
	public void setPlayer(Player player) {
		if (player.hasEntityId()) {
			player = player.clone();
		}
		
		this.player = player;
		this.player.setEntityId(this.getId());
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Boolean hasPlayer() {
		return (this.player != null);
	}
	
	
	
	// TestObject
	public void setTestObject(TestObject testObject) {
		if (testObject.hasEntityId()) {
			testObject = testObject.clone();
		}
		
		this.testObject = testObject;
		this.testObject.setEntityId(this.getId());
	}
	
	public TestObject getTestObject() {
		return this.testObject;
	}
	
	public Boolean hasTestObject() {
		return (this.testObject != null);
	}
	
	
	
	// PlayersAroundMe
	public void setPlayersAroundMe(PlayersAroundMe playersAroundMe) {
		if (playersAroundMe.hasEntityId()) {
			playersAroundMe = playersAroundMe.clone();
		}
		
		this.playersAroundMe = playersAroundMe;
		this.playersAroundMe.setEntityId(this.getId());
	}
	
	public PlayersAroundMe getPlayersAroundMe() {
		return this.playersAroundMe;
	}
	
	public Boolean hasPlayersAroundMe() {
		return (this.playersAroundMe != null);
	}
	
	
	
	// GameCommand
	public void setGameCommand(GameCommand gameCommand) {
		if (gameCommand.hasEntityId()) {
			gameCommand = gameCommand.clone();
		}
		
		this.gameCommand = gameCommand;
		this.gameCommand.setEntityId(this.getId());
	}
	
	public GameCommand getGameCommand() {
		return this.gameCommand;
	}
	
	public Boolean hasGameCommand() {
		return (this.gameCommand != null);
	}
	
	
	
	
	

}
	