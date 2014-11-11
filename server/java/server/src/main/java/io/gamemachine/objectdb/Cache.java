package io.gamemachine.objectdb;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

public class Cache<K,V> {

	private com.google.common.cache.Cache<K, V> cache;

	public Cache(int expiration, int size) {
		this.cache = CacheBuilder.newBuilder().maximumSize(size).expireAfterWrite(expiration, TimeUnit.SECONDS).build();
	}
	
	public void set(K key, V value) {
		this.cache.put(key, value);
	}
	
	public void invalidate(K key) {
		this.cache.invalidate(key);
	}
	
	public void invalidateAll() {
		this.cache.invalidateAll();
	}
	
	public V get(K key) {
		return this.cache.getIfPresent(key);
	}

}
