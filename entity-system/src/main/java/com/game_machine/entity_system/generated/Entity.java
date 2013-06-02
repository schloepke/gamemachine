package com.game_machine.entity_system.generated;

import java.util.HashMap;
import java.util.ArrayList;

public class Entity {


	private ClientConnection clientConnection;

	private ChatMessage chatMessage;

	private Player player;

	private TestObject testObject;

	private PlayersAroundMe playersAroundMe;

	private GameCommand gameCommand;

	private final Integer id;
	
	public Entity(Integer id) {
		this.id = id;
	}
		
	public Integer getId() {
		return this.id;
	}
	
	public ArrayList<String> componentNames() {
		ArrayList<String> names = new ArrayList<String>();

		if (this.hasClientConnection()) {
			names.add(this.clientConnection.getClass().getSimpleName());
		}

		if (this.hasChatMessage()) {
			names.add(this.chatMessage.getClass().getSimpleName());
		}

		if (this.hasPlayer()) {
			names.add(this.player.getClass().getSimpleName());
		}

		if (this.hasTestObject()) {
			names.add(this.testObject.getClass().getSimpleName());
		}

		if (this.hasPlayersAroundMe()) {
			names.add(this.playersAroundMe.getClass().getSimpleName());
		}

		if (this.hasGameCommand()) {
			names.add(this.gameCommand.getClass().getSimpleName());
		}

		return names;
	}
		

	// ClientConnection
	public void setClientConnection(ClientConnection clientConnection) {
		if (clientConnection.hasEntityId()) {
			clientConnection = clientConnection.clone();
		}
		
		this.clientConnection = clientConnection;
		this.clientConnection.setEntityId(this.getId());
	}
	
	public void removeClientConnection() {
		this.clientConnection = null;
	}
	
	public ClientConnection getClientConnection() {
		return this.clientConnection;
	}
	
	public ClientConnection cloneClientConnection() {
		return this.clientConnection.clone();
	}
	
	public Boolean hasClientConnection() {
		return (this.clientConnection != null);
	}
	
	
	

	// ChatMessage
	public void setChatMessage(ChatMessage chatMessage) {
		if (chatMessage.hasEntityId()) {
			chatMessage = chatMessage.clone();
		}
		
		this.chatMessage = chatMessage;
		this.chatMessage.setEntityId(this.getId());
	}
	
	public void removeChatMessage() {
		this.chatMessage = null;
	}
	
	public ChatMessage getChatMessage() {
		return this.chatMessage;
	}
	
	public ChatMessage cloneChatMessage() {
		return this.chatMessage.clone();
	}
	
	public Boolean hasChatMessage() {
		return (this.chatMessage != null);
	}
	
	
	

	// Player
	public void setPlayer(Player player) {
		if (player.hasEntityId()) {
			player = player.clone();
		}
		
		this.player = player;
		this.player.setEntityId(this.getId());
	}
	
	public void removePlayer() {
		this.player = null;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Player clonePlayer() {
		return this.player.clone();
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
	
	public void removeTestObject() {
		this.testObject = null;
	}
	
	public TestObject getTestObject() {
		return this.testObject;
	}
	
	public TestObject cloneTestObject() {
		return this.testObject.clone();
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
	
	public void removePlayersAroundMe() {
		this.playersAroundMe = null;
	}
	
	public PlayersAroundMe getPlayersAroundMe() {
		return this.playersAroundMe;
	}
	
	public PlayersAroundMe clonePlayersAroundMe() {
		return this.playersAroundMe.clone();
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
	
	public void removeGameCommand() {
		this.gameCommand = null;
	}
	
	public GameCommand getGameCommand() {
		return this.gameCommand;
	}
	
	public GameCommand cloneGameCommand() {
		return this.gameCommand.clone();
	}
	
	public Boolean hasGameCommand() {
		return (this.gameCommand != null);
	}
	
	
	

	
	

}
	