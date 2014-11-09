package com.game_machine.core;

import akka.actor.UntypedActor;

public interface IActorFactory {
	UntypedActor create();
}
