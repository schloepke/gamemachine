package io.gamemachine.core;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import akka.actor.UntypedActor;

class ActorFactory implements IndirectActorProducer {
    final String beanName;
    private Object factory;

    public ActorFactory(Object factory, String beanName) {
        this.beanName = beanName;
        this.factory = factory;

    }

    @Override
    public Class<? extends Actor> actorClass() {
        return UntypedActor.class;
    }

    @Override
    public UntypedActor produce() {
        return ((IActorFactory) factory).create();
    }
}