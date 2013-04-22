package com.game_machine.systems;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.GameMessage;
import com.game_machine.server.Server;

public class Game extends UntypedActor {

LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Game() {
		
	}
	
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			log.info("Game GameMessage message: {}", message);
			Server.udpServer.sendMessage((GameMessage)message);
		} else {
			unhandled(message);
		}
	}
}
