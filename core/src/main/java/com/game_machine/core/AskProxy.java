package com.game_machine.core;

import java.util.concurrent.atomic.AtomicLong;
import akka.actor.Props;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskableActorSelection;

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
		log.info("AskProxy targetName = " + targetName);
		this.callable = callable;
		ActorSelection target = GameMachineLoader.getActorSystem()
				.actorSelection(targetName);
		target.tell(object, this.getSelf());
		
		this.context().system().scheduler().scheduleOnce(Duration.create(200, TimeUnit.MILLISECONDS),
				  this.getSelf(), "timeout", this.context().system().dispatcher(), null);
	}

	public void onReceive(Object message) {
		log.info("AskProxy got "+ message.toString());
		this.callable.apply(message);
		this.getContext().stop(this.getSelf());
	}
}
