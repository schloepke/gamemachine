package com.game_machine.persistence;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GameMachine;

public class ObjectDb extends UntypedActor {

	public HashMap<String, GameObject> gameObjects;
	public static int count = 0;
	public static int count2 = 0;
	private ActorRef datastore;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void test2() {
		long start = System.currentTimeMillis();
		
		log.warning("QUERY TIME: " + Long.toString(System.currentTimeMillis() - start));
	}

	public ObjectDb() {
		GameMachine.setActorRef(this.getClass().getSimpleName(), this.getSelf());
		datastore = this.getContext().actorOf(Props.create(WriteBehindPersistence.class), WriteBehindPersistence.class.getSimpleName());
		gameObjects = new HashMap<String, GameObject>();
		gameObjects.put("2", new GameObject());
	}

	public void onReceive(Object message) {
		log.info("Db message: {}", message);
		if (message instanceof Query) {
			
			Query query = (Query) message;
			
			if (query.getType().equals("update")) {
				if (gameObjects.get(query.getGameObjectId()) != null) {
					query.getMapper().apply(gameObjects.get(query.getGameObjectId()));
					datastore.tell(query.getGameObject(),this.getSelf());
				}
			} else if (query.getType().equals("save")) {
				gameObjects.put(query.getGameObjectId(),query.getGameObject());
			} else if (query.getType().equals("find")) {
				this.getSender().tell(gameObjects.get(query.getGameObjectId()),this.getSelf());
				datastore.tell(query.getGameObject(),this.getSelf());
			} else {
				unhandled(message);
			}
			
		} else {
			unhandled(message);
		}
	}
}
