package com.game_machine.systems.memorydb;

import akka.actor.ActorRef;

import com.game_machine.ActorUtil;


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
	private final Callable queryRunner;
	private final String queryType;
	
	
	
	public static final void find(String gameObjectId, ActorRef sender) {
		Query query = new Query(gameObjectId,null,null,"find");
		ActorUtil.getSelectionByClass(Db.class).tell(query, sender);
	}
	
	public static final void findSync(String gameObjectId, ActorRef sender) {
		Query query = new Query(gameObjectId,null,null,"find");
		ActorUtil.getSelectionByClass(Db.class).tell(query, sender);
	}
	
	public static final void save(String gameObjectId, GameObject gameObject) {
		Query query = new Query(gameObjectId,gameObject,null,"save");
		ActorUtil.getSelectionByClass(Db.class).tell(query, null);
	}
	
	public static final void update(String gameObjectId, Callable queryRunner) {
		Query query = new Query(gameObjectId,null,queryRunner,"update");
		ActorUtil.getSelectionByClass(Db.class).tell(query, null);
	}
	
	public Query(String gameObjectId, GameObject gameObject, Callable queryRunner, String type) {
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
	
	public Callable getMapper() {
		return this.queryRunner;
	}
	
	public String getType() {
		return this.queryType;
	}
}
