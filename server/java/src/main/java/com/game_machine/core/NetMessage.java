package com.game_machine.core;

import GameMachine.Messages.ClientMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class NetMessage {

	// Protocols
	public static final int UDP = 0;
	public static final int UDT = 1;
	public static final int TCP = 2;

	public byte[] bytes;
	public ByteBuf buf;
	public ClientMessage clientMessage;
	public final String host;
	public final int port;
	public final int protocol;
	public final ChannelHandlerContext ctx;

	public NetMessage(int protocol, String host, int port, ChannelHandlerContext ctx) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.ctx = ctx;
	}
	
}
