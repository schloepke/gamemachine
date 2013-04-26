package com.game_machine.systems;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.GmKernel;
import com.game_machine.messages.NetMessage;

public class Game extends UntypedActor {

LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Game() {
		
	}
	
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			log.info("Game NetMessage message: {}", message);
			NetMessage nm = Base.agent.get();
			Base.agent.send(new NetMessage(0, 0, null, null, 0));
			//Testing.test1();
			ActorSelection ref;
			ref = this.getContext().actorSelection("/user/db");
			ref.tell(message, this.getSelf());
			
			ref = this.getContext().actorSelection("/user/outbound");
			ref.tell(message, this.getSelf());
		} else if (message instanceof String) {
			Testing.test1();
		} else {
			unhandled(message);
		}
	}
}
