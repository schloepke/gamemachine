package com.game_machine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.dyuproject.protostuff.JsonIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_machine.proto.Entities;
import com.game_machine.proto.Entity;
import com.game_machine.proto.GameCommand;

public class MessageUtil {

	
	public static Entities createEchoCommand() {
		Entities entities = new Entities();
		Entity entity = new Entity();
		ArrayList<Entity> entitiesList= new ArrayList<Entity>();
		entitiesList.add(entity);
		GameCommand gameCommand = new GameCommand();
		gameCommand.setName("Echo");
		entity.setGameCommand(gameCommand);
		entities.setEntityList(entitiesList);
		return entities;
	}
	
	public static Entities loadGameCommands() {
		ObjectMapper mapper = new ObjectMapper();

		String root = System.getProperty("user.dir");
		File file = new File(root + "\\src\\test\\java\\com\\game_machine\\test\\game_commands.json");
		try {
			//all = mapper.readValue(file, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public static byte[] toJson(Entities entities) {
		boolean numeric = false;
		InputStream in;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			//JsonIOUtil.mergeFrom(in, entities, Entities.getSchema(), numeric);
			
			JsonIOUtil.writeTo(out, entities, Entities.getSchema(), numeric);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	public static byte[] toProtobuf(Entities entities) {
		LinkedBuffer buffer = LinkedBuffer.allocate(1024);
		byte[] bytes = null;

		try {
			bytes = ProtobufIOUtil.toByteArray(entities, RuntimeSchema.getSchema(Entities.class), buffer);
			buffer.clear();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Protobuf encoding failed");
		}
		return bytes;
	}
	
	public static Entities decode(byte[] bytes) {
		Entities entities = new Entities();
		ProtobufIOUtil.mergeFrom(bytes, entities, RuntimeSchema.getSchema(Entities.class));
		return entities;
	}
	
	public static byte[] encode(Entities entities) {
		if (Config.messageEncoding.equals("protobuf")) {
			return toProtobuf(entities);
		} else if (Config.messageEncoding.equals("json")) {
			return toJson(entities);
		} else {
			throw new RuntimeException("No encoding specified");
		}
		
	}


}
