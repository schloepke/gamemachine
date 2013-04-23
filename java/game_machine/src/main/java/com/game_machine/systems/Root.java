package com.game_machine.systems;

import java.io.IOException;

import com.game_machine.messages.NetMessage;

import scala.concurrent.ExecutionContext;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.routing.RoundRobinRouter;

public class Root {

	public static ActorSystem system;
	public static ActorRef router;
	public static MemcachedClient memcachedClient;
	public static Agent<NetMessage> agent;
	
	public static Boolean isRunning() {
		if (Root.system == null || Root.system.isTerminated()) {
			return false;
		} else {
			return true;
		}
	}

	public static void initMemcacheClient() {
		try {
			Root.memcachedClient = new MemcachedClient(AddrUtil.getAddresses("192.168.130.128:11211"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Unable to init memcached client");
		}
		
	}
	
	public static void start(String hostname, String port) {
		Root.initMemcacheClient();
		Root.system = ActorUtil.createSystem("system", hostname, port);
		ActorUtil.createSystem("app1", "192.168.1.3", "2553");
		//Root.system = ActorUtil.createSystem("system", null, null);
		
		//Root.system.actorOf(Props.create(Outbound.class), "outbound");
		Root.system.actorOf(Props.create(Inbound.class), "inbound");
		Root.system.actorOf(Props.create(Db.class).withDispatcher("db-dispatcher"), "db");
		
		//Root.system.actorOf(Props.create(Inbound.class).withRouter(new RoundRobinRouter(10)), "inbound");
		Root.system.actorOf(Props.create(Outbound.class).withRouter(new RoundRobinRouter(10)), "outbound");
		
		ExecutionContext ec = ExecutionContexts.global();
		agent = Agent.create(new NetMessage(0, 0, null, port, 0), ec);
	}

	
	
	public static void stop() {
		Root.system.shutdown();
	}
}
