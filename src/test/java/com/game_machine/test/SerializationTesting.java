package com.game_machine.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.game_machine.Entities;
import com.game_machine.Entity;
import com.game_machine.proto.Components;
import com.game_machine.proto.GameCommand;
import com.game_machine.proto.Player;
import com.game_machine.proto.PlayersAroundMe;

public class SerializationTesting {

	private static final Logger log = LoggerFactory.getLogger(SerializationTesting.class);
	
	public Entities testEntities1() {
		Integer entityId = 2000;
		Player player = new Player();
		player.setId(1);
		
		PlayersAroundMe players = new PlayersAroundMe();
		players.setEntityId(entityId);
		players.addPlayer(player);
		
		GameCommand gameCommand = new GameCommand();
		gameCommand.setName("test");
		gameCommand.setEntityId(entityId);
		
		Entity entity = new Entity(entityId);
		entity.addComponent(player);
		entity.addComponent(players);
		entity.addComponent(gameCommand);
		
		Entities entities = new Entities();
		entities.addEntity(entity);
		return entities;
	}
	
	@Test
	public void EntityTest() {
		try {
			Entities.template();
			Entities entities = testEntities1();
			Components components = entities.toComponents();
			assertThat(components.getPlayerCount()).isEqualTo(1);
			assertThat(components.getGameCommandCount()).isEqualTo(1);
			assertThat(components.getPlayersAroundMeCount()).isEqualTo(1);
			
			Entities entities2 = Entities.createFrom(components);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
