package com.game_machine;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class Cmd extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private static ActorRef actor;
	private static SynchronousQueue<String> queue = new SynchronousQueue<String>();
	private static SynchronousQueue<ActorIdentity> identityQueue = new SynchronousQueue<ActorIdentity>();

	public Cmd() {
		Cmd.actor = this.getSelf();
	}

	public void onReceive(Object message) {
		try {
			if (message instanceof String) {
				queue.offer((String) message, 2, TimeUnit.SECONDS);
			} else if (message instanceof ActorIdentity) {
				identityQueue.offer((ActorIdentity) message, 2, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Object ask(Object message, ActorRef actor, Integer ms) {
		Timeout timeout = new Timeout(Duration.create(ms, "ms"));
		Future<Object> future = Patterns.ask(actor, message, timeout);
		Object result = null;
		try {
			result = Await.result(future, timeout.duration());
		} catch (TimeoutException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static ActorRef identify(Class<?> klass) {
		ActorUtil.getSelectionByClass(klass).tell(new Identify("1"), actor);
		ActorIdentity identity = null;
		try {
			identity = identityQueue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return identity.getRef();
	}

	public static String send(String message, Class<?> klass) {
		ActorUtil.getSelectionByClass(klass).tell(message, actor);
		String result = null;
		try {
			result = queue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

}
