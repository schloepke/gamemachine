package com.game_machine.net.client;

import com.game_machine.ProtobufMessages.ClientMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageBuilder {

	public static ClientMessage encode(byte[] bytes, String clientId) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		builder.setBody(ByteString.copyFrom(bytes));
		builder.setClientId(clientId);
		ClientMessage clientMessage = builder.build();
		return clientMessage;
	}

	public static ClientMessage encode(String body, String clientId) {
		return encode(body.getBytes(), clientId);
	}

	public static ClientMessage decode(byte[] bytes) {
		try {
			return ClientMessage.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to code bytes");
		}
	}

}
