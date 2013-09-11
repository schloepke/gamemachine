package com.game_machine.core;

import io.netty.channel.ChannelHandlerContext;

public class NetMessage {

	// Endpoints
	public static final int UDP = 0;
	public static final int UDT = 1;
	public static final int TCP = 2;
	public static final int DISCONNECTED = 3;
		
	public final byte[] bytes;
	public final String host;
	public final int port;
	public final int protocol;
	public final String clientId;
	public final ChannelHandlerContext ctx;
	
	public NetMessage(String clientId, int protocol, byte[] bytes, String host, int port, ChannelHandlerContext ctx) {
		this.clientId = clientId;
		this.bytes = bytes;
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.ctx = ctx;
	}
	
}
