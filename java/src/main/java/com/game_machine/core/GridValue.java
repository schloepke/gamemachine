package com.game_machine.core;

import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.Npc;
import com.game_machine.entity_system.generated.Player;

public class GridValue {

	public String id;
	public int cell;
	public Entity entity;
	public String entityType;
	
	public GridValue(String id, int cell, Entity entity, String entityType) {
		this.id = id;
		this.cell = cell;
		this.entity = entity;
		this.entityType = entityType;
	}
}
