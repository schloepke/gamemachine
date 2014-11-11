package io.gamemachine.core;

import akka.actor.UntypedActor;

public interface IActorFactory {
	UntypedActor create();
}
