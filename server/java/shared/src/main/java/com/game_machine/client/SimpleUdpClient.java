package com.game_machine.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;

import com.game_machine.client.messages.ClientMessage;
import akka.actor.ActorSelection;


public class SimpleUdpClient implements Runnable, NetworkClient {

	public static AtomicInteger messageCount = new AtomicInteger();
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private int port;
	private InetAddress hostAddress;
	private DatagramSocket s;
	private int timeout;
	private boolean stop = false;
	private ActorSelection inbound;
	private DatagramPacket inp;
	
	
	public SimpleUdpClient(String host, int port, String actorName) {
		this.port = port;
		this.timeout = 1000;
		this.inbound = Api.getActorByName(actorName);
		byte[] in = new byte[8192];
		inp = new DatagramPacket(in, in.length, hostAddress, port);
		connect(host,port);
	}
	
	public void start() {
		executor.execute(this);
	}
	
	public void run() {
		byte[] message;
		
		while (true) {
			if (stop) {
				s.disconnect();
				s.close();
				return;
			}
			
			message = receive();
			if (message != null) {
				ClientMessage clientMessage = ApiMessage.parseFrom(message);
				inbound.tell(clientMessage,null);
			}
		}
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public Boolean connect(String host, int port) {
		try {
			hostAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}

		try {
			s = new DatagramSocket();
			s.setSoTimeout(timeout);
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendMessage(byte[] bytes) {
		messageCount.incrementAndGet();
		DatagramPacket out = new DatagramPacket(bytes, bytes.length, hostAddress, port);
		try {
			s.send(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] receive() {
		try {
			s.receive(inp);
			byte[] received = Arrays.copyOfRange(inp.getData(), inp.getOffset(), inp.getOffset() + inp.getLength());
			return received;
		} catch (IOException e) {
			return null;
		}
	}
	
}
