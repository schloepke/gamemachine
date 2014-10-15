package com.game_machine.codeblocks;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.game_machine.codeblocks.api.Codeblock;
import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMachineLoader;

public class CodeblockActor extends UntypedActor {

	public ConcurrentHashMap<String, String> actors = new ConcurrentHashMap<String, String>();
	private static final Logger logger = LoggerFactory.getLogger(CodeblockActor.class);
	
	private Codeblock codeblock;
	private CodeblockExecutor executor;
	
	public static ActorRef start(String gameId, String name, Codeblock codeblock) {
		String actorName = gameId + "_" + name;
		ActorSystem system = GameMachineLoader.getActorSystem();
		ActorRef ref = system.actorOf(Props.create(CodeblockActor.class, gameId, codeblock), actorName);
		return ref;
	}
	
	public static void stop(String name) {
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(akka.actor.PoisonPill.getInstance(), null);
	}
	
	public CodeblockActor(Codeblock codeblock) {
		this.codeblock = codeblock;
		this.executor = new CodeblockExecutor();
		this.executor.setPerms();
		this.executor.runRestricted(this.codeblock, "awake", "awake");
		
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		this.executor.runRestricted(this.codeblock, "run", message);
	}

}
