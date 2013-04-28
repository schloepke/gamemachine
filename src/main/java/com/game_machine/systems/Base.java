package com.game_machine.systems;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import scala.concurrent.ExecutionContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.routing.RoundRobinRouter;

import com.game_machine.Cmd;
import com.game_machine.messages.NetMessage;
import com.game_machine.systems.memorydb.Db;

public class Base {

	public static ActorSystem system;
	public static ActorRef router;
	public static MemcachedClient memcachedClient;
	public static Agent<NetMessage> agent;
	
	public static Boolean isRunning() {
		if (Base.system == null || Base.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}

	public static void initMemcacheClient() {
		try {
			Base.memcachedClient = new MemcachedClient(AddrUtil.getAddresses("192.168.130.128:11211"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to init memcached client");
		}
		
	}
	
	public static void start() {
		Base.initMemcacheClient();
		Base.system = ActorUtil.createSystem("system");
		//ActorUtil.createSystem("app4", "192.168.1.3", "2553");
		//Root.system = ActorUtil.createSystem("system", null, null);
		
		//Root.system.actorOf(Props.create(Outbound.class), "outbound");
		//Root.system.actorOf(Props.create(Inbound.class), "inbound");
		Base.system.actorOf(Props.create(Db.class).withDispatcher("db-dispatcher"), "db");
		
		Base.system.actorOf(Props.create(Inbound.class).withRouter(new RoundRobinRouter(10)), "inbound");
		Base.system.actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), "outbound");
		Base.system.actorOf(Props.create(ServerMonitor.class), "serverMonitor");
		
		Cmd.start();
		ExecutionContext ec = ExecutionContexts.global();
		agent = Agent.create(new NetMessage(0, 0, null, "1234", 0), ec);
	}
	
	public static void stop() {
		Base.system.shutdown();
	}
	
}
