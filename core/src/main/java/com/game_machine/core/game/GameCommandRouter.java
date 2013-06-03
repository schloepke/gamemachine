package com.game_machine.core.game;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.Entity;
import com.game_machine.entity_system.generated.GameCommand;

public class GameCommandRouter extends GameActor {

	private HashMap<String, ActorRef> actors = new HashMap<String, ActorRef>();

	public GameCommandRouter() {
		actors.put(Combat.class.getSimpleName(),
				this.getContext().actorOf(Props.create(Combat.class), Combat.class.getSimpleName()));
		actors.put(Location.class.getSimpleName(),
				this.getContext().actorOf(Props.create(Location.class), Location.class.getSimpleName()));
		ActorRef a = this.getContext().actorOf(Props.create(Echo.class), Echo.class.getSimpleName());
		actors.put(Echo.class.getSimpleName(), a);
	}

	public void onReceive(Entities entities) {
		
	}
}
