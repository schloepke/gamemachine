package com.game_machine.core.game;

import com.game_machine.entity_system.generated.Entity;

public class Echo extends GameActor {

	public void onReceive(Entity entity) {
		log.info("Echo Onreceive");
		Gateway.send(getClientId(), entity);
	}
}
