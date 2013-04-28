package com.game_machine.systems.memorydb;

public final class QueryResponse {

	private final GameObject gameObject;
	
	public QueryResponse(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	public final GameObject getGameObject() {
		return this.gameObject;
	}
}
