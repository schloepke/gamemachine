package com.game_machine.core;
import akka.actor.IndirectActorProducer;
import akka.actor.UntypedActor;
import akka.actor.Actor;

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
    return ((IActorFactory)factory).create();
  }
}