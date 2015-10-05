package io.gamemachine.net.udp;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.MathHelper;
import io.gamemachine.core.NetMessage;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.routing.Incoming;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

/*
 * This is mostly an experiment to see the difference between a simple udp server and one using NIO.  End result almost no difference at all.  
 */
public class SimpleUdpServer implements Runnable {

	public class ClientAddress {
		public InetAddress address;
		public int port;
		
		public ClientAddress(InetAddress address, int port) {
			this.address = address;
			this.port = port;
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleUdpServer.class);
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private static ConcurrentHashMap<Long,ClientAddress> clients = new ConcurrentHashMap<Long,ClientAddress>();
	private int port;
	private int threadCount = 5;
	private ActorSelection inbound;
	private static DatagramSocket serverSocket = null;

	public static void start(String host, int port) {
		SimpleUdpServer server = new SimpleUdpServer(host, port);
		server.start();
	}
	
	public static void stop() {
		
	}
	
	public SimpleUdpServer(String host, int port) {
		this.port = 24130;
		this.inbound = ActorUtil.getSelectionByName(Incoming.name);
		try {
			serverSocket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public static void removeClient(long clientId) {
		if (clients.containsKey(clientId)) {
			clients.remove(clientId);
		}
	}
	
	public static void sendMessage(long clientId, byte[] bytes) {
		ClientAddress clientAddress = clients.get(clientId);
		if (clientAddress == null) {
			logger.warn("ClientAddress not found for "+clientId);
			return;
		}
		DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, clientAddress.address, clientAddress.port);
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		for (int i = 0; i < threadCount; ++i) {
			executor.execute(this);
		}
	}

	public void run() {
		byte[] receiveData = new byte[2048];
		while (true) {
			DatagramPacket inp = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(inp);
				byte[] received = Arrays.copyOfRange(inp.getData(), inp.getOffset(), inp.getOffset() + inp.getLength());
				int ip = ByteBuffer.wrap(inp.getAddress().getAddress()).getInt();
				long clientId = MathHelper.cantorize(ip,inp.getPort());
				
				logger.debug("MessageReceived length" + received.length + " " + new String(received));
				ClientAddress clientAddress = new ClientAddress(inp.getAddress(), inp.getPort());
				ClientMessage clientMessage = ClientMessage.parseFrom(received);
				
				if (clientMessage.sentAt > 0) {
					long latency = System.currentTimeMillis() - clientMessage.getSentAt();
					if (latency >= 4) {
						logger.info("ClientMessage latency " + latency);
					}
				}
				
				if (!clients.containsKey(clientId)) {
					clients.put(clientId, clientAddress);
				}
				
				NetMessage netMessage = new NetMessage(NetMessage.SIMPLE_UDP, ip, clientId);
				netMessage.clientMessage = clientMessage;

				this.inbound.tell(netMessage, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
