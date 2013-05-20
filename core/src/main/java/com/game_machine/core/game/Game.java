package com.game_machine.core.game;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.core.ActorUtil;

public class Game extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private HashMap<String, ActorRef> actors = new HashMap<String, ActorRef>();
	
	public Game() {
		actors.put("combat", this.getContext().actorOf(Props.create(Combat.class), Combat.class.getSimpleName()));
		actors.put("location", this.getContext().actorOf(Props.create(Location.class), Location.class.getSimpleName()));
		
		
	}

	public ArrayList<String> getCommandList() {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("combat");
		commands.add("location");
		return commands;
	}

	public void onReceive(Object message) {
		if (message instanceof Object) {
			log.debug("Game NetMessage message: {}");
			ActorUtil.getSelectionByClass(Echo.class).tell(message, this.getSelf());
			
			//ActorRef a = Cmd.identify(Echo.class);
			//Cmd.ask("echo echo", a);
			
			//Query.find("2",5);
			
			//ActorRef commandActor;
			//for (String command : getCommandList()) {
			//	commandActor = actors.get(command);
			//	commandActor.tell(command,this.getSelf());
			//}
			
			//ActorSelection ref;
			//ref = this.getContext().actorSelection("/user/db");
			//ref.tell(new Query(), this.getSelf());

			//ActorUtil.getSelectionByClass(Outbound.class).tell(message, this.getSelf());
		} else if (message instanceof String) {
		} else {
			unhandled(message);
		}
	}
}
