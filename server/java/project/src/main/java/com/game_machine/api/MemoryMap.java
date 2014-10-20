package com.game_machine.api;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

public class MemoryMap {

	private HTreeMap<String, byte[]> cache;
	
	public MemoryMap(double size) {
		DB db = DBMaker.newMemoryDirectDB().transactionDisable().make();
		cache = db.createHashMap("cache").expireStoreSize(size).counterEnable().keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.BYTE_ARRAY).make();
	}
	
	public HTreeMap<String, byte[]> getMap() {
		return cache;
	}
}
