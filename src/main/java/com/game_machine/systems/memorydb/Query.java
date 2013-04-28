package com.game_machine.systems.memorydb;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import com.game_machine.systems.Base;

public final class Query {

	private final String gameObjectId;
	private final GameObject gameObject;
	private final QueryRunner mapper;
	private final String queryType;

	public static final ActorSelection getTarget() {
		return  Base.system.actorSelection("/user/db");
	}
	
	public static final void find(String gameObjectId, ActorRef sender) {
		Query query = new Query(gameObjectId,null,null,"find");
		getTarget().tell(query, sender);
	}
	
	public static final void save(String gameObjectId, GameObject gameObject) {
		Query query = new Query(gameObjectId,gameObject,null,"save");
		getTarget().tell(query, null);
	}
	
	public static final void update(String gameObjectId, QueryRunner mapper) {
		Query query = new Query(gameObjectId,null,mapper,"update");
		getTarget().tell(query, null);
	}
	
	public Query(String gameObjectId, GameObject gameObject, QueryRunner mapper, String type) {
		this.gameObjectId = gameObjectId;
		this.gameObject = gameObject;
		this.mapper = mapper;
		this.queryType = type;
	}
		
	public String getGameObjectId() {
		return this.gameObjectId;
	}
	
	public GameObject getGameObject() {
		return this.gameObject;
	}
	
	public QueryRunner getMapper() {
		return this.mapper;
	}
	
	public String getType() {
		return this.queryType;
	}
}
