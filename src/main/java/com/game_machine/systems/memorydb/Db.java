package com.game_machine.systems.memorydb;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;

public class Db extends UntypedActor {

	public HashMap map;
	public static int count = 0;
	public static int count2 = 0;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Db() {
		log.warning("Db started");
		map = new HashMap();
		try {
			Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onReceive(Object message) throws Exception {
		if (message instanceof Query) {
				Method m = QueryDefinitions.class.getMethod(((Query) message).getName(), HashMap.class);
				m.invoke(new QueryDefinitions(),map);
		} else {
			unhandled(message);
		}
	}
}
