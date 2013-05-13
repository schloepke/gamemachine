
package com.game_machine.entity_system.generated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.Entity;
import com.game_machine.entity_system.Component;

import com.game_machine.entity_system.generated.Components;

import com.game_machine.entity_system.generated.Player;

import com.game_machine.entity_system.generated.PlayersAroundMe;

import com.game_machine.entity_system.generated.GameCommand;



public class EntitiesHelper {

	private static final Logger log = LoggerFactory.getLogger(Entities.class);


	public static Components entitiesToComponents(Entities entities) {
		Components components = new Components();
		for (Entity entity : entities.getEntities().values()) {
			for (Map.Entry<String, Component> entry : entity.getComponents().entrySet()) {
				
				
				if (entry.getKey().equals("Player")) {
					components.addPlayer(Player.class.cast(entry.getValue()));
				}
				
				
				if (entry.getKey().equals("PlayersAroundMe")) {
					components.addPlayersAroundMe(PlayersAroundMe.class.cast(entry.getValue()));
				}
				
				
				if (entry.getKey().equals("GameCommand")) {
					components.addGameCommand(GameCommand.class.cast(entry.getValue()));
				}
				
			}
		}
		return components;
	}
}

