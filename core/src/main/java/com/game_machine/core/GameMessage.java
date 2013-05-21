package com.game_machine.core;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;

public class GameMessage {

	private final String clientId;
	private final Entities entities;
	private final Entity entity;
	
	public GameMessage(String clientId, Entity entity, Entities entities) {
		this.clientId = clientId;
		this.entities = entities;
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public Boolean hasEntity() {
		return (this.entity != null);
	}
	
	public Entities getEntities() {
		return this.entities;
	}
	
	public Boolean hasEntities() {
		return (this.entities != null);
	}
	
	public String getClientId() {
		return this.clientId;
	}
}
