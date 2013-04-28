package com.game_machine.systems.memorydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Db extends UntypedActor {

	public HashMap<String, GameObject> gameObjects;
	public static int count = 0;
	public static int count2 = 0;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void test2() {
		long start = System.currentTimeMillis();
		
		log.warning("QUERY TIME: " + Long.toString(System.currentTimeMillis() - start));
	}

	public Db() {
		log.warning("Db started " + this.getSelf().path().toString());
		gameObjects = new HashMap<String, GameObject>();
		Query.update("test", new QueryRunner() {
			public GameObject apply(GameObject gameObject) {
				return new Player();
			}
		});
	}

	public void onReceive(Object message) {
		if (message instanceof Query) {
			
			Query query = (Query) message;
			
			if (query.getType().equals("update")) {
				if (gameObjects.get(query.getGameObjectId()) != null) {
					query.getMapper().apply(gameObjects.get(query.getGameObjectId()));
				}
			} else if (query.getType().equals("save")) {
				gameObjects.put(query.getGameObjectId(),query.getGameObject());
			} else if (query.getType().equals("find")) {
				this.getSender().tell(gameObjects.get(query.getGameObjectId()),this.getSelf());
			} else {
				unhandled(message);
			}
			
		} else {
			unhandled(message);
		}
	}
}
