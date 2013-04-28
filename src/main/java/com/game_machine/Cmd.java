package com.game_machine;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.core.actors.Root;
import com.game_machine.core.actors.UdpServerManager;

public class Cmd extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private static ActorRef actor;
	private static SynchronousQueue<String> queue = new SynchronousQueue<String>();

	public Cmd() {
		Cmd.actor = this.getSelf();
	}

	public void onReceive(Object message) {
		try {
			queue.offer((String) message,2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static ActorRef start() {
		return Root.system.actorOf(Props.create(Cmd.class), "commands");
	}
	
	public static String send(String message, Class klass) {
		ActorUtil.getActorByClass(klass).tell(message, actor);
		String result = null;
		try {
			result = queue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String startUdpServer() {
		return send(UdpServerManager.CMD_START,UdpServerManager.class);
	}
}
