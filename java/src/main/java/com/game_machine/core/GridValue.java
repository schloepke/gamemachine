package com.game_machine.core;

import com.game_machine.entity_system.generated.Entity;

public class GridValue {

	public final String id;
	public final int cell;
	public final float x;
	public final float y;
	public final float z;
	
	public final String entityType;
	public long createdAt;

	public GridValue(String id, int cell, float x, float y, float z, String entityType) {
		this.id = id;
		this.cell = cell;
		this.x = x;
		this.y = y;
		this.z = z;
		this.entityType = entityType;
		//this.createdAt = System.nanoTime();
	}
}
