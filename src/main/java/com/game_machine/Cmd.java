package com.game_machine;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.server.UdpManager;
import com.game_machine.systems.Base;

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
		return Base.system.actorOf(Props.create(Cmd.class), "commands");
	}

	public static ActorSelection getRefByName(String name) {
		return Base.system.actorSelection("/user/" + name);
	}
	
	public static String send(String message, String name) {
		getRefByName(name).tell(message, actor);
		String result = null;
		try {
			result = queue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String startUdpServer() {
		return send(UdpManager.CMD_START,"serverMonitor");
	}
}
