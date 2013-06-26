package com.game_machine.core;

import java.net.InetSocketAddress;
import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Udp;
import akka.io.UdpMessage;
import akka.japi.Procedure;
import akka.util.ByteString;

public class UdpListener extends UntypedActor {
	 
	private HashMap<String,InetSocketAddress> clients = new HashMap<String,InetSocketAddress>();
	
	  public UdpListener() {
	    
	    // request creation of a bound listen socket
	    final ActorRef mgr = Udp.get(getContext().system()).getManager();
	    mgr.tell(
	        UdpMessage.bind(getSelf(), new InetSocketAddress("localhost", 8100)),
	        getSelf());
	  }
	 
	  @Override
	  public void onReceive(Object msg) {
	    if (msg instanceof Udp.Bound) {
	      final Udp.Bound b = (Udp.Bound) msg;
	      getContext().become(ready(getSender()));
	    } else unhandled(msg);
	  }
	  
	  private Procedure<Object> ready(final ActorRef socket) {
	    return new Procedure<Object>() {
	      @Override
	      public void apply(Object msg) throws Exception {
	        if (msg instanceof Udp.Received) {
	          final Udp.Received r = (Udp.Received) msg;
	          ActorUtil.getSelectionByName("GameMachine::UdpServerActor").tell(r, getSelf());
	        } else if (msg instanceof UdpMessage) {
	        	UdpMessage udpMessage = (UdpMessage) msg;
	        	socket.tell(udpMessage, getSelf());
	        } else if (msg.equals(UdpMessage.unbind())) {
	          socket.tell(msg, getSelf());
	        
	        } else if (msg instanceof Udp.Unbound) {
	          getContext().stop(getSelf());
	          
	        } else unhandled(msg);
	      }
	    };
	  }
	}