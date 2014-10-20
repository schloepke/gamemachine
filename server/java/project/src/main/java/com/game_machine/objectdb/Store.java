package com.game_machine.objectdb;

import java.util.HashMap;

import com.game_machine.core.EntitySerializer;
import com.game_machine.core.PersistableMessage;

public class Store {

	private static HashMap<String, Class<?>> classCache = new HashMap<String, Class<?>>();
	private Storable store;
	private String serialization;
	private long cacheWritesPerSecond = -1;
	private long cacheWriteInterval = -1;

	private Store() {

	}

	private static class LazyHolder {
		private static final Store INSTANCE = new Store();
	}

	public static Store getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public void connect(String storeName, String serialization, long cacheWritesPerSecond, long cacheWriteInterval) {
		this.serialization = serialization;
		this.setCacheWriteInterval(cacheWriteInterval);
		this.setCacheWritesPerSecond(cacheWritesPerSecond);
		
		if (storeName.equals("gamecloud")) {
			this.store = (Storable) new CloudStore();
			this.store.connect();
		} else if (storeName.equals("jdbc")) {
			this.store = (Storable) new JdbcStore();
			this.store.connect();
		} else if (storeName.equals("couchbase")) {
			this.store = (Storable) new CouchbaseStore();
			this.store.connect();
		} else if (storeName.equals("memory")) {
			this.store = (Storable) new MemoryStore();
			this.store.connect();
		}
	}
	
	public static Class<?> getKlass(String classname) throws ClassNotFoundException {
		if (classCache.containsKey(classname)) {
			return classCache.get(classname);
		} else {
			Class<?> clazz = Class.forName("GameMachine.Messages." + classname);
			classCache.put(classname, clazz);
			return clazz;
		}
	}

	public Object get(String id) throws ClassNotFoundException {
		return get(id,"Entity");
	}
	
	public Object get(String id, String classname) throws ClassNotFoundException {
		Class<?> clazz = getKlass(classname);
		if (serialization.equals("json")) {
			String stringValue = this.store.getString(id);
			if (stringValue == null) {
				return null;
			}
			return EntitySerializer.fromJson(stringValue, clazz);
		} else {
			byte[] byteValue = this.store.getBytes(id);
			if (byteValue == null) {
				return null;
			}
			return EntitySerializer.fromByteArray(byteValue, clazz);
		}
	}

	public void delete(String id) {
		this.store.delete(id);
	}

	public void set(String id, PersistableMessage message) {
		if (serialization.equals("json")) {
			this.store.setString(id, message.toJson());
		} else {
			this.store.setBytes(id, message.toByteArray());
		}
	}

	public void shutdown() {
		this.store.shutdown();
	}

	public long getCacheWritesPerSecond() {
		return cacheWritesPerSecond;
	}

	public void setCacheWritesPerSecond(long cacheWritesPerSecond) {
		this.cacheWritesPerSecond = cacheWritesPerSecond;
	}

	public long getCacheWriteInterval() {
		return cacheWriteInterval;
	}

	public void setCacheWriteInterval(long cacheWriteInterval) {
		this.cacheWriteInterval = cacheWriteInterval;
	}
	
	
}
