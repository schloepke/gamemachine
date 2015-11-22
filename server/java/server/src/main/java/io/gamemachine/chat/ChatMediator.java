package io.gamemachine.chat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.contrib.pattern.DistributedPubSubMediator;
import akka.routing.RandomRoutingLogic;
import io.gamemachine.core.GameMachineLoader;

public class ChatMediator {

	public ConcurrentHashMap<String, ActorRef> mediators = new ConcurrentHashMap<String, ActorRef>();

	private ChatMediator() {

	}

	private static class LazyHolder {
		private static final ChatMediator INSTANCE = new ChatMediator();
	}

	public static ChatMediator getInstance() {
		return LazyHolder.INSTANCE;
	}

	public synchronized ActorRef get(String name) {
		if (!mediators.containsKey(name)) {
			create(name);
		}
		return mediators.get(name);
	}
	
	public void create(String name) {
		FiniteDuration gossipInterval = FiniteDuration.create(1, TimeUnit.SECONDS);
		FiniteDuration removedTimeToLive = FiniteDuration.create(120, TimeUnit.SECONDS);
		int maxDeltaElements = 3000;
		ActorRef mediator = GameMachineLoader.getActorSystem().actorOf(
				DistributedPubSubMediator.props("", new RandomRoutingLogic(), gossipInterval,
						removedTimeToLive, maxDeltaElements), name);
		mediators.put(name, mediator);
	}

}
