package com.game_machine.entity_system.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.GameCommand;
import com.game_machine.entity_system.generated.Player;
import com.game_machine.entity_system.generated.PlayersAroundMe;

public class EntityTest {

	private static final Logger log = LoggerFactory.getLogger(EntityTest.class);
	
	@Test
	public void clone_should_remove_entity_id() {
		Player player = new Player();
		player.setId(1);
		player.setEntityId(2);
		assertThat(player.clone().getEntityId()).isNull();
	}
	
	@Test
	public void set_component_should_set_entity_id_on_component() {
		Entity entity = new Entity(1);
		entity.setPlayer(new Player());
		assertThat(entity.getPlayer().getEntityId()).isEqualTo(1);
	}
	
	
}
