package com.game_machine.client;

import GameMachine.Messages.ChatChannel;
import GameMachine.Messages.ChatMessage;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.EchoTest;
import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.GameMessages;
import GameMachine.Messages.Player;
import GameMachine.Messages.PlayerConnect;
import GameMachine.Messages.PlayerLogout;


public class ApiMessage {

	private String playerId;
	private String authtoken;
	private String defaultId = "0";
	private ClientMessage currentMessage;
	private NetworkClient networkClient;
	
	public ApiMessage(String playerId, String authtoken, NetworkClient networkClient) {
		this.playerId = playerId;
		this.authtoken = authtoken;
		this.networkClient = networkClient;
	}
	
	public void sendBytes(byte[] bytes) {
		networkClient.sendMessage(bytes);
	}
	
	public void send() {
		networkClient.sendMessage(currentMessage.toByteArray());
		this.currentMessage = null;
	}
	
	public ApiMessage setGroupChatMessage(String channel, String message) {
		return setChatMessage("group",channel,message);
	}
	
	public ApiMessage setPrivateChatMessage(String recipientId, String message) {
		return setChatMessage("private",recipientId,message);
	}
	
	public ApiMessage setChatMessage(String type, String channel, String message) {
		ChatMessage chatMessage = new ChatMessage();
		ChatChannel chatChannel = new ChatChannel();
		chatChannel.setName(channel);
		chatMessage.setChatChannel(chatChannel);
		chatMessage.setMessage(message);
		chatMessage.setType(type);
		chatMessage.setSenderId(playerId);
		currentMessage = clientMessage().addEntity(entity().setChatMessage(chatMessage));
		return this;
	}
	
	public ApiMessage setDestination(String destination) {
		currentMessage.getEntity(0).setDestination(destination);
		return this;
	}
	
	public ApiMessage setEchoTest() {
		EchoTest echoTest = new EchoTest().setMessage("echo");
		currentMessage = clientMessage().addEntity(entity().setEchoTest(echoTest));
		return this;
	}
	
	public ApiMessage setGameMessage(GameMessage gameMessage) {
		GameMessages gameMessages = new GameMessages();
		gameMessages.addGameMessage(gameMessage);
		Entity entity = entity();
		entity.setGameMessages(gameMessages);
		currentMessage = clientMessage().addEntity(entity);
		return this;
	}
	
	public Entity entity() {
		return new Entity().setId(defaultId);
	}
	
	public ClientMessage clientMessage() {
		Player player = new Player();
		player.setId(playerId).setAuthtoken(authtoken);
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setConnection_type(0);
		clientMessage.setPlayer(player);
		return clientMessage;
	}
	
	public ApiMessage setConnect() {
		currentMessage =  clientMessage().setPlayerConnect(new PlayerConnect());
		return this;
	}
	
	public ApiMessage setLogin(String password) {
		PlayerConnect playerConnect = new PlayerConnect();
		playerConnect.setPlayerId(this.playerId).setPassword(password);
		currentMessage =  clientMessage().setPlayerConnect(new PlayerConnect());
		return this;
	}
	
	public ApiMessage setLogout() {
		PlayerLogout playerLogout = new PlayerLogout();
		playerLogout.setAuthtoken(authtoken).setPlayerId(playerId);
		currentMessage = clientMessage().setPlayerLogout(playerLogout);
		return this;
	}

}
