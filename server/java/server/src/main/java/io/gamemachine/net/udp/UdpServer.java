package io.gamemachine.net.udp;

public class UdpServer {

	public static void start(String type, String host, Integer port) {
		if (type.equals("simple")) {
			SimpleUdpServer.start(host,port);
		} else if (type.equals("netty")) {
			NettyUdpServer.start(host,port);
		}
	}
	
	public static void stop(String type) {
		if (type.equals("simple")) {
			SimpleUdpServer.stop();
		} else if (type.equals("netty")) {
			NettyUdpServer.stop();
		}
	}
}
