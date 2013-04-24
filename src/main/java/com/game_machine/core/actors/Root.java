package com.game_machine.core.actors;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

import com.game_machine.ActorUtil;
import com.game_machine.Cmd;
import com.game_machine.systems.memorydb.Db;

public class Root extends UntypedActor {

	public static ActorSystem system;

	public Root() {
		this.getContext().actorOf(Props.create(Db.class).withDispatcher("db-dispatcher"), Db.class.getSimpleName());

		this.getContext().actorOf(Props.create(Inbound.class).withRouter(new RoundRobinRouter(10)), Inbound.class.getSimpleName());
		this.getContext().actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), Outbound.class.getSimpleName());
		this.getContext().actorOf(Props.create(UdpServerManager.class), UdpServerManager.class.getSimpleName());

		this.getContext().actorOf(Props.create(Cmd.class), Cmd.class.getSimpleName());
	}

	public static void start() {
		Root.system = ActorUtil.createSystem("system");
		Root.system.actorOf(Props.create(Root.class), Root.class.getSimpleName());
	}

	public static void shutdown() {
		Root.system.shutdown();
	}

	public void onReceive(Object message) {
		unhandled(message);
	}

}
