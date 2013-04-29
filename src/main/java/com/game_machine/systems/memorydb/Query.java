package com.game_machine.systems.memorydb;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;

import com.game_machine.GameMachine;


/*
 Query.update("test", new QueryRunner() {
	public GameObject apply(GameObject gameObject) {
		return new Player();
	}
});

*/


public final class Query {

	private final String gameObjectId;
	private final GameObject gameObject;
	private final QueryRunner queryRunner;
	private final String queryType;
	
	
	public static final ActorSelection getTarget() {
		return GameMachine.actorSystem.actorSelection("/user/db");
	}
	
	public static final void find(String gameObjectId, ActorRef sender) {
		Query query = new Query(gameObjectId,null,null,"find");
		getTarget().tell(query, sender);
	}
	
	public static final void save(String gameObjectId, GameObject gameObject) {
		Query query = new Query(gameObjectId,gameObject,null,"save");
		getTarget().tell(query, null);
	}
	
	public static final void update(String gameObjectId, QueryRunner queryRunner) {
		Query query = new Query(gameObjectId,null,queryRunner,"update");
		getTarget().tell(query, null);
	}
	
	public Query(String gameObjectId, GameObject gameObject, QueryRunner queryRunner, String type) {
		this.gameObjectId = gameObjectId;
		this.gameObject = gameObject;
		this.queryRunner = queryRunner;
		this.queryType = type;
	}
		
	public String getGameObjectId() {
		return this.gameObjectId;
	}
	
	public GameObject getGameObject() {
		return this.gameObject;
	}
	
	public QueryRunner getMapper() {
		return this.queryRunner;
	}
	
	public String getType() {
		return this.queryType;
	}
}
