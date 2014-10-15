package com.game_machine.client;

import akka.actor.ActorSystem;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.EchoTest;
import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.GameMessages;
import GameMachine.Messages.Player;
import GameMachine.Messages.PlayerConnect;
import GameMachine.Messages.PlayerLogout;


public class MessageApi {

	private String playerId;
	private String authtoken;
	private String defaultId = "0";
	private ClientMessage currentMessage;
	private NetworkClient networkClient;
	private static ActorSystem actorSystem;
	
	public MessageApi(String playerId, String authtoken, NetworkClient networkClient) {
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
	
	public MessageApi echoTest() {
		EchoTest echoTest = new EchoTest().setMessage("echo");
		currentMessage = clientMessage().addEntity(entity().setEchoTest(echoTest));
		return this;
	}
	
	public MessageApi gameMessage(GameMessage gameMessage) {
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
	
	public MessageApi connect() {
		currentMessage =  clientMessage().setPlayerConnect(new PlayerConnect());
		return this;
	}
	
	public MessageApi logout() {
		PlayerLogout playerLogout = new PlayerLogout();
		playerLogout.setAuthtoken(authtoken).setPlayerId(playerId);
		currentMessage = clientMessage().setPlayerLogout(playerLogout);
		return this;
	}

	public static ActorSystem getActorSystem() {
		return actorSystem;
	}

	public static void setActorSystem(ActorSystem actorSystem) {
		MessageApi.actorSystem = actorSystem;
	}
}
