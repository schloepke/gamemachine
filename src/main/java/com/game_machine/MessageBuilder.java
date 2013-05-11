package com.game_machine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_machine.Pb.ClientMessage;
import com.game_machine.Pb.GameCommand;
import com.game_machine.Pb.Item;
import com.game_machine.Pb.Player;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageBuilder {

	@SuppressWarnings("unchecked")
	public static ArrayList<GameCommand> loadGameCommands() {
		ArrayList<GameCommand> gameCommands = new ArrayList<GameCommand>();
		ObjectMapper mapper = new ObjectMapper();

		String root = System.getProperty("user.dir");
		File file = new File(root + "\\src\\test\\java\\com\\game_machine\\test\\game_commands.json");
		HashMap<String, ArrayList<HashMap>> all = null;
		try {
			all = mapper.readValue(file, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Map.Entry<String, ArrayList<HashMap>> entry : all.entrySet()) {
			for (HashMap m : entry.getValue()) {
				gameCommands.add(MapToGameCommand(entry.getKey(), m));
			}
		}
		return gameCommands;
	}

	public static GameCommand MapToGameCommand(String command, Map<String, String> map) {
		GameCommand.Builder builder = GameCommand.newBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Item.Builder itemBuilder = Item.newBuilder();
			builder.setName(command);
			itemBuilder.setKey(entry.getKey());
			itemBuilder.setValue(entry.getValue());
			builder.addItem(itemBuilder.build());

		}
		return builder.build();
	}

	
	public static byte[] encodeMessage(GameModel message, Class<GameModel> klass) {
		LinkedBuffer buffer = LinkedBuffer.allocate(512);
		byte[] protostuff = null;

		try {
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
		builder.addPlayers(Player.newBuilder().setName(playerId).build());
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
