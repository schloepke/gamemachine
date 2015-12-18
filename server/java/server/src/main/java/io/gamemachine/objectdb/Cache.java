package io.gamemachine.objectdb;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

public class Cache<K,V> {

	private com.google.common.cache.Cache<K, V> cache;

	public Cache(int expiration, int size) {
		cache = CacheBuilder.newBuilder().maximumSize(size).expireAfterWrite(expiration, TimeUnit.SECONDS).build();
	}
	
	public void set(K key, V value) {
		cache.put(key, value);
	}
	
	public void put(K key, V value) {
		cache.put(key, value);
	}
	
	public void invalidate(K key) {
		cache.invalidate(key);
	}
	
	public void invalidateAll() {
		cache.invalidateAll();
	}
	
	public V get(K key) {
		return cache.getIfPresent(key);
	}
	
	public Map<K,V> asMap() {
		return cache.asMap();
	}
	
	public long size() {
		return cache.size();
	}

}
