package com.game_machine.objectdb;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.mapdb.HTreeMap;

import GameMachine.Messages.Entity;
import GameMachine.Messages.ObjectdbDel;
import GameMachine.Messages.ObjectdbGet;
import GameMachine.Messages.ObjectdbPut;
import GameMachine.Messages.ObjectdbUpdate;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import com.game_machine.api.MemoryMap;
import com.game_machine.core.ActorUtil;
import com.game_machine.core.EntitySerializer;
import com.game_machine.core.PersistableMessage;

public class DbActor extends UntypedActor {
	
	private static HashMap<Class<?>, Method> methods = new HashMap<Class<?>, Method>();
	private HTreeMap<String, byte[]> cache;
	private Store store;
	
	public DbActor() {
		this.store = Store.getInstance();
		MemoryMap memoryMap = new MemoryMap(0.100);
		this.cache = memoryMap.getMap();
	}
	
	private void setMessage(String id, PersistableMessage message) {
		cache.put(id, message.toByteArray());
		ActorSelection sel = ActorUtil.findDistributed("GameMachine::WriteBehindCache", id);
		sel.tell(message, getSelf());
	}
	
	public void setEntity(Entity entity) {
		cache.put(entity.getId(), entity.toByteArray());
		ActorSelection sel = ActorUtil.findDistributed("GameMachine::WriteBehindCache", entity.getId());
		sel.tell(entity, getSelf());
	}
	
	public void deleteEntity(String id) {
		cache.remove(id);
		store.delete(id);
	}
	
	public Object getEntity(String id, String classname) throws ClassNotFoundException {
		if (cache.containsKey(id)) {
			Class<?> clazz = Class.forName("GameMachine.Messages."+classname);
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
			ObjectdbUpdate update = (ObjectdbUpdate)message;
			ActorSelection sel = ActorUtil.findDistributed("GameMachine::ObjectDb", update.getCurrentEntityId());
			sel.tell(message, getSelf());
		} else if (message instanceof ObjectdbPut) {
			ObjectdbPut put = (ObjectdbPut)message;
			setEntity(put.getEntity());
			getSender().tell(true, getSelf());
		} else if (message instanceof ObjectdbGet) {
			ObjectdbGet get = (ObjectdbGet)message;
			Object obj = getEntity(get.getEntityId(),get.getKlass());
			getSender().tell(obj, getSelf());
		} else if (message instanceof ObjectdbDel) {
			ObjectdbDel del = (ObjectdbDel)message;
			deleteEntity(del.getEntityId());
		} else {
			PersistableMessage persistable = (PersistableMessage)message;
			setMessage(persistable.getId(),persistable);
			getSender().tell(true, getSelf());
		}
	}

}
