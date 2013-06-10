package com.game_machine.core;

import java.io.File;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.GameCommand;

public class MessageUtil {

	public static Entities createEchoCommand() {
		Entities entities = new Entities();
		Entity entity;
		for (int i = 1; i < 20; i++) {
			entity = new Entity(i);
			GameCommand gameCommand = new GameCommand();
			gameCommand.setName("Echo");
			entity.setGameCommand(gameCommand);
			entities.addEntity(entity);
		}
		return entities;
	}

}
