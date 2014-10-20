package com.game_machine.client.api;

import Client.Messages.ChatChannel;
import Client.Messages.ChatMessage;
import Client.Messages.ClientMessage;
import Client.Messages.EchoTest;
import Client.Messages.Entity;
import Client.Messages.GameMessage;
import Client.Messages.GameMessages;
import Client.Messages.Player;
import Client.Messages.PlayerConnect;
import Client.Messages.PlayerLogout;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;


public class ApiMessage {

	private String playerId;
	private String authtoken;
	private String defaultId = "0";
	private Api base;
	private ClientMessage clientMessage;

	public ApiMessage(String playerId, String authtoken, Api base) {
		this.playerId = playerId;
		this.authtoken = authtoken;
		this.base = base;
		this.clientMessage = clientMessage();
	}

	public static ClientMessage parseFrom(byte[] bytes) {
		ClientMessage message = new ClientMessage();
		ProtobufIOUtil.mergeFrom(bytes, message, RuntimeSchema.getSchema(ClientMessage.class));
		return message;
	}
	
	public static byte[] toByteArray(ClientMessage clientMessage) {
		LinkedBuffer buffer = CachedLinkedBuffer.get();
		byte[] bytes = null;

		try {
			bytes = ProtobufIOUtil.toByteArray(clientMessage, RuntimeSchema.getSchema(ClientMessage.class), buffer);
			buffer.clear();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Protobuf encoding failed");
		}
		return bytes;
	}
	

	public void send() {
		byte[] bytes = toByteArray(clientMessage);
		base.send(bytes);
		if (clientMessage.getEntityCount() >= 1) {
			clientMessage.getEntityList().clear();
		}
	}

	public ApiMessage setGroupChatMessage(String channel, String message) {
		return setChatMessage("group", channel, message);
	}

	public ApiMessage setPrivateChatMessage(String recipientId, String message) {
		return setChatMessage("private", recipientId, message);
	}

	public ApiMessage setChatMessage(String type, String channel, String message) {
		ChatMessage chatMessage = new ChatMessage();
		ChatChannel chatChannel = new ChatChannel();
		chatChannel.setName(channel);
		chatMessage.setChatChannel(chatChannel);
		chatMessage.setMessage(message);
		chatMessage.setType(type);
		chatMessage.setSenderId(playerId);
		clientMessage.addEntity(entity().setChatMessage(chatMessage));
		return this;
	}

	public ApiMessage setDestination(String destination) {
		clientMessage.getEntity(0).setDestination(destination);
		return this;
	}

	public ApiMessage setEchoTest() {
		EchoTest echoTest = new EchoTest().setMessage("echo");
		clientMessage.addEntity(entity().setEchoTest(echoTest));
		return this;
	}

	public ApiMessage setGameMessage(GameMessage gameMessage) {
		GameMessages gameMessages = new GameMessages();
		gameMessages.addGameMessage(gameMessage);
		Entity entity = entity();
		entity.setGameMessages(gameMessages);
		clientMessage.addEntity(entity);
		return this;
	}

	public Entity entity() {
		return new Entity().setId(defaultId);
	}

	public ClientMessage clientMessage() {
		Player player = new Player();
		player.setId(playerId).setAuthtoken(authtoken);
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setConnectionType(0);
		clientMessage.setPlayer(player);
		return clientMessage;
	}

	public ApiMessage setConnect() {
		clientMessage.setPlayerConnect(new PlayerConnect());
		return this;
	}

	public ApiMessage setLogin(String password) {
		PlayerConnect playerConnect = new PlayerConnect();
		playerConnect.setPlayerId(this.playerId).setPassword(password);
		clientMessage.setPlayerConnect(new PlayerConnect());
		return this;
	}

	public ApiMessage setLogout() {
		PlayerLogout playerLogout = new PlayerLogout();
		playerLogout.setAuthtoken(authtoken).setPlayerId(playerId);
		clientMessage.setPlayerLogout(playerLogout);
		return this;
	}

}
