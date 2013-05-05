package com.game_machine.persistence;

import akka.actor.ActorRef;

import com.game_machine.ActorUtil;
import com.game_machine.Cmd;


/*
 Query.update("test", new Callable() {
	public GameObject apply(GameObject gameObject) {
		return new Player();
	}
});

*/


public final class Query {

	private final String gameObjectId;
	private final GameObject gameObject;
	private final Callable callable;
	private final String queryType;
	
	
	
	public static final void findAsync(String gameObjectId, ActorRef sender) {
		Query query = new Query(gameObjectId,null,null,"find");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, sender);
	}
	
	public static final GameObject find(String gameObjectId, Integer timeout) {
		Query query = new Query(gameObjectId,null,null,"find");
		return (GameObject) Cmd.ask(query, ActorUtil.getActorByClass(ObjectDb.class), timeout);
	}
	
	public static final void save(String gameObjectId, GameObject gameObject) {
		Query query = new Query(gameObjectId,gameObject,null,"save");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, null);
	}
	
	public static final void update(String gameObjectId, Callable callable) {
		Query query = new Query(gameObjectId,null,callable,"update");
		ActorUtil.getSelectionByClass(ObjectDb.class).tell(query, null);
	}
	
	public Query(String gameObjectId, GameObject gameObject, Callable callable, String type) {
		this.gameObjectId = gameObjectId;
		this.gameObject = gameObject;
		this.callable = callable;
		this.queryType = type;
	}
		
	public String getGameObjectId() {
		return this.gameObjectId;
	}
	
	public GameObject getGameObject() {
		return this.gameObject;
	}
	
	public Callable getMapper() {
		return this.callable;
	}
	
	public String getType() {
		return this.queryType;
	}
}
