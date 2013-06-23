package com.game_machine.core;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSelection;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.ClusterDomainEvent;
import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class EventStreamHandler extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public EventStreamHandler() {
		this.getContext().system().eventStream()
				.subscribe(this.getSelf(), DeadLetter.class);

		
		if (this.getContext().system().name().equals("cluster")) {
			// Add subscription of cluster events
			Cluster.get(this.getContext().system()).subscribe(getSelf(),
					ClusterDomainEvent.class);
			log.info("Subscribing to cluster events");
		}
		
	}

	@Override
	public void onReceive(Object message) throws Exception {
		ActorSelection sel = ActorUtil
				.getSelectionByName("GameMachine::SystemMonitor");
		sel.tell(message, this.getSelf());
		

	}
}
