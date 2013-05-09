package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_machine.MessageBuilder;
import com.game_machine.Pb.GameCommands;



public class GameCommandsTest {


	@Test
	public void gameCommands() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("name", "combat");
		map.put("key1", "value1");
		
		ObjectMapper mapper = new ObjectMapper();
		
		String root = System.getProperty("user.dir");
		File file = new File(root+"\\src\\test\\java\\com\\game_machine\\test\\game_commands.json");
		System.out.println(root);
		try {
			map = (HashMap) mapper.readValue(file, HashMap.class).get("game_commands");
			GameCommands gameCommands = GameCommands.newBuilder().addGameCommand(MessageBuilder.MapToGameCommand(map)).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
