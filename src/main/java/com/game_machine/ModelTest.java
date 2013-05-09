package com.game_machine;

import com.dyuproject.protostuff.Tag;

public final class ModelTest extends GameModel{

	@Tag(1)
	private final String name;
	
	public ModelTest(String name) {
		this.name = name;
	}
	
	
	
	public String getName() {
		return this.name;
	}
}
