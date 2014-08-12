package com.game_machine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.routing.ConsistentHash;

public class Hashring {

	private static final Logger log = LoggerFactory.getLogger(Hashring.class);
	public static ConcurrentHashMap<String, Hashring> hashrings = new ConcurrentHashMap<String, Hashring>();
	
	private ConsistentHash<String> hash;
	public String name;
	public List<String> nodes = new ArrayList<String>();
	
	public static void dump() {
		for (String key : hashrings.keySet()) {
			Hashring ring = hashrings.get(key);
			log.warn("Ring: "+key);
			for (String node : ring.nodes) {
				log.warn("Node: "+node);
			}
		}
	}
	
	public static void addHashring(String name, Hashring ring) {
		hashrings.put(name, ring);
	}
	
	public static void removeHashring(String name) {
		hashrings.remove(name);
	}
	
	public static Hashring getHashring(String name) {
		return hashrings.get(name);
	}
	
	public Hashring(String name, List<String> nodes, int vnodes) {
		for (String node : nodes) {
			this.nodes.add(node);
		}
		this.name = name;
		hash = ConsistentHash.create(nodes, vnodes);
		addHashring(name,this);
	}
	
	public void addNode(String node) {
		hash = hash.add(node);
		nodes.add(node);
	}
	
	public void removeNode(String node) {
		hash = hash.remove(node);
		nodes.remove(node);
	}
	
	public String nodeFor(String key) {
		return hash.nodeFor(key);
	}
	
}
