package com.game_machine.server;

import java.util.logging.Level;


public class Server {

	public static UdpServer udpServer = null;
	
	public static void writeUdp() {
		//udpServer.sendMessage(message, host, port)
	}
	
	public static void startUdpServer(String hostname, int port, Level level) {
		UdpServer.logLevel = level;
		udpServer = new UdpServer(hostname, 1234);
		udpServer.start();
	}
	
	public static void stopUdpServer() {
		udpServer.stop();
		udpServer = null;
	}
}
