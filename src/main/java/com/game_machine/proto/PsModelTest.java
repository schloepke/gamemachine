package com.game_machine.proto;

import com.dyuproject.protostuff.Tag;

public final class PsModelTest extends GameModel{

	@Tag(1)
	private final String name;
	
	public PsModelTest(String name) {
		this.name = name;
	}
	
	
	
	public String getName() {
		return this.name;
	}
}
