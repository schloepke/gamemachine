package com.game_machine.core;

import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import GameMachine.Messages.Entity;
import GameMachine.Messages.ObjectdbDel;
import GameMachine.Messages.ObjectdbGet;
import GameMachine.Messages.ObjectdbPut;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

public class DatastoreCommands {
	
	public static void put(Entity entity) {
		ActorSelection sel = ActorUtil.findDistributed("GameMachine::ObjectDb", entity.id);
		ObjectdbPut put = new ObjectdbPut().setEntity(entity);
		sel.tell(put, null);
	}
	
	public static void delete(String id) {
		ActorSelection sel = ActorUtil.findDistributed("GameMachine::ObjectDb", id);
		ObjectdbDel del = new ObjectdbDel().setEntityId(id);
		sel.tell(del, null);
	}
	
	public static Entity get(String id, int timeout) {
		ObjectdbGet get = new ObjectdbGet().setEntityId(id);
		ActorSelection sel = ActorUtil.findDistributed("GameMachine::ObjectDb", id);
		Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
		AskableActorSelection askable = new AskableActorSelection(sel);
		Future<Object> future = askable.ask(get,t);
		try {
			Entity result = (Entity) Await.result(future, t.duration());
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}
