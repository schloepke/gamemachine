package com.game_machine;

import com.game_machine.core.net.UdpServer;

public class GmContext {

	public static UdpServer udpServer;
	
	public static void setUdpServer(UdpServer udpServer) {
		GmContext.udpServer = udpServer;
	}
	
	public static UdpServer getUdpServer() {
		return GmContext.udpServer;
	}
}
