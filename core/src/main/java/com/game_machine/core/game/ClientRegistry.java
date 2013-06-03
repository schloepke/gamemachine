package com.game_machine.core.game;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.entity_system.Component;
import com.game_machine.entity_system.generated.ClientConnection;

public class ClientRegistry extends GameActor {

	private HashMap<String, Long> clients = new HashMap<String, Long>();

	public ClientRegistry() {
		
	}

	public void onReceive(String command) {
		//log.info("CheckExpired");
	}

	public void onReceive(Component component) {
		
	}

	
}
