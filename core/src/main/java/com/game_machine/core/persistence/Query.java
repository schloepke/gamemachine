package com.game_machine.core.persistence;

import akka.actor.ActorRef;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.Cmd;
import com.game_machine.entity_system.generated.Entity;


/*
 Query.update("test", new Callable() {
	public entity apply(entity entity) {
		return new Player();
	}
});

*/


public final class Query {

	private final Integer entityId;
	private final Entity entity;
	private final Callable callable;
	private final String queryType;
	
	
	
	public static final void findAsync(Integer entityId, ActorRef sender) {
		Query query = new Query(entityId,null,null,"find");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, sender);
	}
	
	public static final Entity find(Integer entityId, Integer timeout) {
		Query query = new Query(entityId,null,null,"find");
		return (Entity) Cmd.ask(query, ActorUtil.getActorByClass(ObjectDb.class), timeout);
	}
	
	public static final void save(Integer entityId, Entity entity) {
		Query query = new Query(entityId,entity,null,"save");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, null);
	}
	
	public static final void update(Integer entityId, Callable callable) {
		Query query = new Query(entityId,null,callable,"update");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, null);
	}
	
	public Query(Integer entityId, Entity entity, Callable callable, String type) {
		this.entityId = entityId;
		this.entity = entity;
		this.callable = callable;
		this.queryType = type;
	}
		
	public Integer getEntityId() {
		return this.entityId;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public Callable getMapper() {
		return this.callable;
	}
	
	public String getType() {
		return this.queryType;
	}
}
