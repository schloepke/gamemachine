package com.game_machine.client.api;

/*
 *  Provides a simple api for sending messages that takes care of much of the boilerplate message creation.  You should obtain ApiMessage instances by
 *  calling newMessage on an Api instance, rather then instantiating it directly.
 */


import Client.Messages.AgentTrackData;
import Client.Messages.ChatChannel;
import Client.Messages.ChatMessage;
import Client.Messages.ChatStatus;
import Client.Messages.ClientMessage;
import Client.Messages.EchoTest;
import Client.Messages.Entity;
import Client.Messages.GameMessage;
import Client.Messages.GameMessages;
import Client.Messages.JoinChat;
import Client.Messages.LeaveChat;
import Client.Messages.Player;
import Client.Messages.PlayerConnect;
import Client.Messages.PlayerLogout;
import Client.Messages.TrackData;

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
		base.sendToNetwork(bytes);
		if (clientMessage.getEntityCount() >= 1) {
			clientMessage.getEntityList().clear();
		}
	}

	public ApiMessage setLeaveChat(String name) {
		LeaveChat leaveChat = new LeaveChat();

		// You can add multiple channels to your LeaveChat message, here we just
		// add one
		ChatChannel chatChannel = new ChatChannel();
		chatChannel.setName(name);
		leaveChat.addChatChannel(chatChannel);
		clientMessage.addEntity(entity().setLeaveChat(leaveChat));
		return this;
	}

	public ApiMessage setJoinChat(String name, String inviteId) {
		JoinChat joinChat = new JoinChat();

		ChatChannel chatChannel = new ChatChannel();
		chatChannel.setName(name);

		String flags = "subscribers";
		chatChannel.setFlags(flags);

		if (inviteId != null) {
			chatChannel.setInviteId(inviteId);
		}
		joinChat.addChatChannel(chatChannel);
		clientMessage.addEntity(entity().setJoinChat(joinChat));
		return this;
	}

	public ApiMessage setJoinPrivateChat(String name, String inviteId) {
		return setJoinChat(name,inviteId);
	}

	public ApiMessage setJoinPublicChat(String name) {
		return setJoinChat(name,null);
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

	public ApiMessage setChatStatus() {
		clientMessage.addEntity(entity().setChatStatus(new ChatStatus()));
		return this;
	}
	
	public ApiMessage setDestination(String destination) {
		clientMessage.getEntity(0).setDestination(destination);
		return this;
	}

	public ApiMessage setEchoTest() {
		EchoTest echoTest = new EchoTest().setMessage("echo");
		clientMessage.addEntity(entity().setEchoTest(echoTest));
		//this.setDestination("GameMachine/GameSystems/RemoteEcho");
		return this;
	}

	public ApiMessage setTrackData(TrackData trackData) {
		ensureEntity();
		clientMessage.getEntity(0).setTrackData(trackData);
		clientMessage.getEntity(0).setFastpath(true);
		return this;
	}
	
	public ApiMessage setAgentTrackData(AgentTrackData agentTrackData) {
		ensureEntity();
		clientMessage.getEntity(0).setAgentTrackData(agentTrackData);
		clientMessage.getEntity(0).setFastpath(true);
		return this;
	}
	
	public <T> ApiMessage setDynamicMessage(T message) {
		GameMessage gameMessage = DynamicMessageUtil.toGameMessage(message);
		return setGameMessage(gameMessage);
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
	
	public void ensureEntity() {
		if (clientMessage.getEntityCount() == 0) {
			clientMessage.addEntity(entity());
		}
	}

}
