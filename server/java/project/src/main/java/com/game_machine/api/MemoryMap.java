package com.game_machine.api;

import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class MemoryMap {

	private Map<String, byte[]> cache;
	
	private static class LazyHolder {
		private static final MemoryMap INSTANCE = new MemoryMap();
	}

	public static MemoryMap getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private MemoryMap() {
		DB db = DBMaker.newMemoryDirectDB().transactionDisable().make();
		this.cache = db.createHashMap("cache").expireStoreSize(1).counterEnable().keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.BYTE_ARRAY).make();
	}
	
	public Map<String, byte[]> getMap() {
		return cache;
	}
}
