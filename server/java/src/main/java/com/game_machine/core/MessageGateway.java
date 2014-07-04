package com.game_machine.core;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MessageGateway extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection udpIncoming;
	
	public MessageGateway()
	{
		udpIncoming = ActorUtil
				.getSelectionByName("GameMachine::Endpoints::UdpIncoming");
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
		NetMessage netMessage = (NetMessage)message;
		udpIncoming.tell(message, null);
	}

}
