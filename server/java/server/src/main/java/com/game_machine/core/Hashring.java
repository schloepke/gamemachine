package com.game_machine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.consistent_hashing.KetamaNodeLocator;

public class Hashring {

	private static final Logger log = LoggerFactory.getLogger(Hashring.class);
	public static ConcurrentHashMap<String, Hashring> hashrings = new ConcurrentHashMap<String, Hashring>();

	private KetamaNodeLocator hash;
	public String name;
	public List<String> nodes = new ArrayList<String>();

	public static void dump() {
		for (String key : hashrings.keySet()) {
			Hashring ring = hashrings.get(key);
			log.warn("Ring: " + key);
			for (String node : ring.nodes) {
				log.warn("Node: " + node);
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
		hash = new KetamaNodeLocator(nodes);
		addHashring(name, this);
	}

	public void addNode(String node) {
		nodes.add(node);
		hash.updateLocator(nodes);
	}

	public void removeNode(String node) {
		nodes.remove(node);
		hash.updateLocator(nodes);
	}

	public String nodeFor(String key) {
		return hash.getPrimary(key);
	}

}
