package com.game_machine.objectdb;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.Entity;
import GameMachine.Messages.ObjectdbDel;
import GameMachine.Messages.ObjectdbGet;
import GameMachine.Messages.ObjectdbPut;
import GameMachine.Messages.ObjectdbUpdate;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.game_machine.api.MemoryMap;
import com.game_machine.core.ActorUtil;
import com.game_machine.core.AppConfig;
import com.game_machine.core.EntitySerializer;
import com.game_machine.core.PersistableMessage;

public class DbActor extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(DbActor.class);
	public static final AtomicInteger cacheHits = new AtomicInteger();
	private Map<String, byte[]> cache;
	private Store store;
	private ActorRef writeBehindCache;
	private boolean cacheEnabled = false;

	public DbActor() {
		this.cache = MemoryMap.getInstance().getMap();
		this.store = Store.getInstance();
		if (AppConfig.Datastore.getCacheWriteInterval() >= 1 || AppConfig.Datastore.getCacheWritesPerSecond() >= 1) {
			cacheEnabled = true;
		}
	}

	private void setMessage(String id, PersistableMessage message) {
		cache.put(id, message.toByteArray());
		if (cacheEnabled) {
			writeBehindCache.tell(message, getSelf());
		} else {
			store.set(id, message);
		}
	}

	public void setEntity(Entity entity) {
		cache.put(entity.getId(), entity.toByteArray());
		if (cacheEnabled) {
			writeBehindCache.tell(entity, getSelf());
		} else {
			store.set(entity.getId(), entity);
		}
	}

	public void deleteEntity(String id) {
		cache.remove(id);
		store.delete(id);
	}

	public Object getEntity(String id, String classname) throws ClassNotFoundException {
		if (cache.containsKey(id)) {
			cacheHits.incrementAndGet();
			Class<?> clazz = Class.forName("GameMachine.Messages." + classname);
			return EntitySerializer.fromByteArray(cache.get(id), clazz);
		} else {
			return store.get(id, classname);
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ObjectdbUpdate) {
			ObjectdbUpdate update = (ObjectdbUpdate) message;
			ActorSelection sel = ActorUtil.findDistributed("GameMachine::ObjectDb", update.getCurrentEntityId());
			sel.tell(message, getSelf());
		} else if (message instanceof ObjectdbPut) {
			ObjectdbPut put = (ObjectdbPut) message;
			setEntity(put.getEntity());
			getSender().tell(true, getSelf());
		} else if (message instanceof ObjectdbGet) {
			ObjectdbGet get = (ObjectdbGet) message;
			String classname = get.getKlass();
			if (classname == null) {
				classname = "Entity";
			}
			Object obj = getEntity(get.getEntityId(), classname);
			if (obj != null) {
				getSender().tell(obj, getSelf());
			}
		} else if (message instanceof ObjectdbDel) {
			ObjectdbDel del = (ObjectdbDel) message;
			deleteEntity(del.getEntityId());
		} else {
			PersistableMessage persistable = (PersistableMessage) message;
			setMessage(persistable.getId(), persistable);
			getSender().tell(true, getSelf());
		}
	}

	@Override
	public void preStart() {
		if (cacheEnabled) {
			createCacheChild();
		}
	}

	private void createCacheChild() {
		String name = "write_behind_cache_" + getSelf().path().name();
		writeBehindCache = context().actorOf(Props.create(WriteBehindCache.class, store), name);
	}

}
