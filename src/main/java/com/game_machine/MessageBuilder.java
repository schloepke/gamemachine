package com.game_machine;

import java.util.Map;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.Pb.ClientMessage;
import com.game_machine.Pb.GameCommand;
import com.game_machine.Pb.Item;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageBuilder {

	public static GameCommand MapToGameCommand(Map<String,String> map) {
		GameCommand.Builder builder = GameCommand.newBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Item.Builder itemBuilder = Item.newBuilder();
			if (entry.getKey().equals("name")) {
				builder.setName(entry.getValue());
			} else {
				itemBuilder.setKey(entry.getKey());
				itemBuilder.setValue(entry.getValue());
				builder.addItems(itemBuilder.build());
			}
			
		}
		return builder.build();
	}
	
	public static byte[] encodeMessage(GameModel message, Class<GameModel> klass) {
		LinkedBuffer buffer = LinkedBuffer.allocate(512);
		byte[] protostuff = null;
		
		try
		{
			protostuff = ProtostuffIOUtil.toByteArray(message, RuntimeSchema.getSchema(klass), buffer);
		    buffer.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return protostuff;
	}
	
	public static ClientMessage encode(byte[] bytes, String playerId) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		builder.setBody(ByteString.copyFrom(bytes));
		builder.setPlayerId(playerId);
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
			throw new RuntimeException("Unable to decode bytes " + new String(bytes));
		}
	}

}
