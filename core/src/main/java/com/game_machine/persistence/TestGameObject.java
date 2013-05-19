package com.game_machine.persistence;

public class TestGameObject implements GameObject {

	final private String id;
	
	public TestGameObject(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
}
