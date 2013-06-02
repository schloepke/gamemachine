package com.game_machine.core.persistence;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.entity_system.generated.Entity;

public class ObjectDb extends UntypedActor {

	public HashMap<Integer, Entity> entities;
	public static int count = 0;
	public static int count2 = 0;
	private ActorRef datastore;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void test2() {
		long start = System.currentTimeMillis();

		log.warning("QUERY TIME: " + Long.toString(System.currentTimeMillis() - start));
	}

	public ObjectDb() {
		datastore = this.getContext().actorOf(Props.create(WriteBehindHandler.class, 5000, 50),
				WriteBehindHandler.class.getSimpleName());
		entities = new HashMap<Integer, Entity>();
	}

	public void onReceive(Object message) {
		log.debug("Db message: {}", message);
		if (message instanceof Query) {

			Query query = (Query) message;

			if (query.getType().equals("update")) {
				if (entities.get(query.getEntityId()) != null) {
					Entity entity = entities.get(query.getEntityId());
					query.getMapper().apply(entity);
					datastore.tell(entity, this.getSelf());
				}
			} else if (query.getType().equals("put")) {
				entities.put(query.getEntityId(), query.getEntity());
			} else if (query.getType().equals("get")) {
				Entity entity = entities.get(query.getEntityId());
				if (entity != null) {
					this.getSender().tell(entity, this.getSelf());
				}
			} else {
				unhandled(message);
			}

		} else {
			unhandled(message);
		}
	}
}
