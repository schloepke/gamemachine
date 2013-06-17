package com.game_machine.core.persistence;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.AskCallable;
import com.game_machine.core.AskProxy;
import com.game_machine.core.GameMachineLoader;
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
	private final QueryCallable callable;
	private final String queryType;

	public static final void get(String path, Integer entityId, ActorRef sender) {
		Query query = new Query(entityId, null, null, "get");
		ActorSelection target = GameMachineLoader.getActorSystem()
				.actorSelection(path);
		target.tell(query, sender);
	}

	public static final void get(String path, Integer entityId,
			AskCallable callable) {
		Query query = new Query(entityId, null, null, "get");
		AskProxy.ask(query, path, callable);
	}

	public static final void put(String path, Integer entityId, Entity entity) {
		Query query = new Query(entityId, entity, null, "put");
		ActorSelection target = GameMachineLoader.getActorSystem()
				.actorSelection(path);
		target.tell(query, null);
	}

	public static final void update(String path, Integer entityId,
			QueryCallable callable) {
		Query query = new Query(entityId, null, callable, "update");
		ActorSelection target = GameMachineLoader.getActorSystem()
				.actorSelection(path);
		target.tell(query, null);
	}

	public Query(Integer entityId, Entity entity, QueryCallable callable,
			String type) {
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

	public QueryCallable getMapper() {
		return this.callable;
	}

	public String getType() {
		return this.queryType;
	}
}
