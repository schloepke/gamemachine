package com.game_machine.core;

import java.util.concurrent.atomic.AtomicLong;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AskProxy extends UntypedActor {

	private AskCallable callable;
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private static AtomicLong idCounter = new AtomicLong();

	public static String createID()
	{
	    return String.valueOf(idCounter.getAndIncrement());
	}

	public static void ask(Object message, String targetName, AskCallable callable) {
		GameMachineLoader.getActorSystem().actorOf(Props.create(AskProxy.class, message, targetName, callable),
				AskProxy.class.getSimpleName() + createID());

	}

	public AskProxy(Object object, String targetName, AskCallable callable) {
		this.callable = callable;
		ActorSelection target = ActorUtil.getSelectionByName(targetName);
		target.tell(object, this.getSelf());
	}

	public void onReceive(Object message) {
		this.callable.apply(message);
		this.getContext().stop(this.getSelf());
	}
}
