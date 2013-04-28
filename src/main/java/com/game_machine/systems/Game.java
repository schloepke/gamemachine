package com.game_machine.systems;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;

public class Game extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private HashMap<String, ActorRef> actors = new HashMap<String, ActorRef>();
	
	public Game() {
		actors.put("combat", this.getContext().actorOf(Props.create(Combat.class), "combat"));
		actors.put("location", this.getContext().actorOf(Props.create(Location.class), "location"));
	}

	public ArrayList<String> getCommandList() {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("attack");
		commands.add("update_location");
		return commands;
	}

	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			log.info("Game NetMessage message: {}", message);

			ActorRef commandActor;
			for (String command : getCommandList()) {
				commandActor = actors.get(command);
				commandActor.tell(command,this.getSelf());
			}
			
			ActorSelection ref;
			//ref = this.getContext().actorSelection("/user/db");
			//ref.tell(new Query(), this.getSelf());

			ref = this.getContext().actorSelection("/user/outbound");
			ref.tell(message, this.getSelf());
		} else if (message instanceof String) {
		} else {
			unhandled(message);
		}
	}
}
