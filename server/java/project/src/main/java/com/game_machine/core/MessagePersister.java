package com.game_machine.core;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class MessagePersister extends UntypedActor {

	public static String name = "message_persister";
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof PersistentMessage) {
			PersistentMessage persistentMessage = (PersistentMessage)message;
			String action = persistentMessage.getPersistAction();
			if (action.equals("save")) {
				persistentMessage.dbSave(persistentMessage.getPersistPlayerId());
			} else if (action.equals("delete")) {
				persistentMessage.dbDelete(persistentMessage.getPersistPlayerId());
			}
		}
	}
}
