
package com.game_machine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.proto.Components;
import com.game_machine.proto.GameCommand;
import com.game_machine.proto.Player;
import com.game_machine.proto.PlayersAroundMe;

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

