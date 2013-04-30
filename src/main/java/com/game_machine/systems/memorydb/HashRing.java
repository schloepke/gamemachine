package com.game_machine.systems.memorydb;

import java.util.HashMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashRing {

	private static HashMap<String,HashRing> rings = new HashMap<String,HashRing>();
	
	private HashMap<Integer,String> ring;
	private Integer bucketCount;
	private String name;
	private HashFunction hashFunction = Hashing.md5();
	
	
	public HashRing(String name, Integer bucketCount) {
		this.name = name;
		this.bucketCount = bucketCount;
		ring = new HashMap<Integer,String>();
		for (int i=0;i<bucketCount;i++) {
			ring.put(i,name+Integer.toString(i));
		}
		rings.put(name, this);
	}
	
	public String stringToBucket(String value) {
		Integer bucket = Hashing.consistentHash(hashFunction.hashString(value), bucketCount);
		return ring.get(bucket);
	}
	
	public static String stringToBucket(String value, String ringName) {
		HashRing ring = rings.get(ringName);
		Integer bucket = Hashing.consistentHash(ring.hashFunction.hashString(value), ring.bucketCount);
		return ring.ring.get(bucket);
	}
	
	public static HashRing getRingByName(String name) {
		return rings.get(name);
	}
}
