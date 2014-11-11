package io.gamemachine.net;

import java.net.InetSocketAddress;

import io.gamemachine.messages.ClientConnection;
import io.netty.channel.ChannelHandlerContext;

public class ClientInfo {

	public String host;
	public int port;
	public InetSocketAddress address;
	public ChannelHandlerContext ctx;
	public ClientConnection clientConnection;
	
	public ClientInfo(String host, int port, InetSocketAddress address, ChannelHandlerContext ctx, ClientConnection clientConnection) {
		this.host = host;
		this.port = port;
		this.address = address;
		this.ctx = ctx;
		this.clientConnection = clientConnection;
	}
	
}
