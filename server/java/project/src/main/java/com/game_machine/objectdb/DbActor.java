package com.game_machine.objectdb;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.mapdb.HTreeMap;

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
import com.game_machine.core.EntitySerializer;
import com.game_machine.core.PersistableMessage;

public class DbActor extends UntypedActor {

	private static HashMap<Class<?>, Method> methods = new HashMap<Class<?>, Method>();
	private static HTreeMap<String, byte[]> cache = new MemoryMap(0.100).getMap();
	private Store store;
	private ActorRef writeBehindCache;
	private boolean cacheEnabled = false;

	public DbActor() {
		this.store = Store.getInstance();
		if (store.getCacheWriteInterval() >= 1 || store.getCacheWritesPerSecond() >= 1) {
			cacheEnabled = true;
		}
		// MemoryMap memoryMap = new MemoryMap(0.100);
		// cache = memoryMap.getMap();
	}

	public static HTreeMap<String, byte[]> getCache() {
		return cache;
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
			Class<?> clazz = Class.forName("GameMachine.Messages." + classname);
			return EntitySerializer.fromByteArray(cache.get(id), clazz);
		} else {
			return store.get(id, classname);
		}
	}

	private String getObjectId(Object object) throws Exception {
		Class<?> klass = object.getClass();
		if (!methods.containsKey(klass)) {
			methods.put(klass, klass.getMethod("getId"));
		}
		Method method = methods.get(klass);
		String id = (String) method.invoke(object);
		return id;
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
			Object obj = getEntity(get.getEntityId(), get.getKlass());
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
