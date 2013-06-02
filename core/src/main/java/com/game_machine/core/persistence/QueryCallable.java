package com.game_machine.core.persistence;

import com.game_machine.entity_system.generated.Entity;

public interface QueryCallable {

	public Entity apply(Entity entity);
	
}
