package com.game_machine.systems;

import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;

public class Db extends UntypedActor {

	public static final HashMap map = new HashMap();
	public static int count = 0;
	public static int count2 = 0;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			count++;
			count2++;
			if (count >= 10000) {
				log.warning(Integer.toString(count));
				count = 0;
			}
			if (count2 >= 100) {
				log.warning(Integer.toString(count));
				count2 = 0;
			}
			log.info("Db message: {}", message);
			Db.map.put(count, message);
			Db.map.get(count);
			//Testing.test1();
			
		} else if (message instanceof String) {
			//Testing.test1();
		} else {
			unhandled(message);
		}
	}
}
