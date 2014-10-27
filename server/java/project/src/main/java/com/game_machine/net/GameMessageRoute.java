package com.game_machine.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameMessageRoute {

	public static final Map<String,GameMessageRoute> routes = new ConcurrentHashMap<String,GameMessageRoute>();
	
	private final String id;
	private final String name;
	private final String to;
	private final boolean distributed;
	
	public GameMessageRoute(String id, String name, String to, boolean distributed) {
		this.id = id;
		this.name = name;
		this.to = to;
		this.distributed = distributed;
	}

	public static void add(String id, String name, String to, boolean distributed) {
		GameMessageRoute route = new GameMessageRoute(id,name,to,distributed);
		routes.put(id, route);
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTo() {
		return to;
	}

	public boolean isDistributed() {
		return distributed;
	}
}
