package com.game_machine.game;

import java.util.HashMap;

import akka.actor.UntypedActor;

public class ClientTracker extends UntypedActor {

	private HashMap<String,Boolean> clients = new HashMap<String,Boolean>();
	
	public void onReceive(Object message) throws Exception {
		
	}
}
