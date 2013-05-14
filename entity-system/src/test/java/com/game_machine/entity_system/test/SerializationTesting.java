package com.game_machine.entity_system.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.Entity;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.GameCommand;
import com.game_machine.entity_system.generated.Player;
import com.game_machine.entity_system.generated.PlayersAroundMe;

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
			Entities entities = testEntities1();
			Components components = Components.fromEntities(entities);
			assertThat(components.getPlayerCount()).isEqualTo(1);
			assertThat(components.getGameCommandCount()).isEqualTo(1);
			assertThat(components.getPlayersAroundMeCount()).isEqualTo(1);
			
			entities = components.toEntities();
			assertThat(entities.getEntities().size()).isEqualTo(1);
			Entity entity = entities.getEntity(2000);
			assertThat(entity).isNotNull();
			//Player player = entity.getComponent("Player");
			//assertThat(player).isNotNull();
			//assertThat(player.getId()).isEqualTo(1);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
