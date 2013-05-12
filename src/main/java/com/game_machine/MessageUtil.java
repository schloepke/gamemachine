package com.game_machine;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_machine.proto.Entities;
import com.game_machine.proto.Entity;
import com.game_machine.proto.GameCommand;

public class MessageUtil {

	
	public static Entities createEchoCommand() {
		Entities entities = new Entities();
		Entity entity = new Entity();
		GameCommand gameCommand = new GameCommand();
		gameCommand.setName("Echo");
		entity.setGameCommand(gameCommand);
		entities.addEntity(entity);
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

	

	


}
