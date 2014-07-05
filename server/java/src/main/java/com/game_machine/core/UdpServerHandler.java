package com.game_machine.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;
import akka.actor.ActorSelection;

//@Sharable
public final class UdpServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger log = LoggerFactory
			.getLogger(UdpServerHandler.class);
	public ChannelHandlerContext ctx = null;
	private ActorSelection inbound;

	private HashMap<String,String> clients = new HashMap<String,String>();
	
	public UdpServerHandler() {
		this.inbound = ActorUtil
				.getSelectionByName("message_gateway");
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx,
			final Throwable cause) {
		log.info("close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("UDP server active");
		this.ctx = ctx;
	}

	public void send(byte[] bytes, String host, int port,
			ChannelHandlerContext ctx) {
			//log.info("send " + "host: " + host + " port: " + port + " bytes: "
			//		+ bytes.toString()+" ctx: "+ctx);

			ByteBuf buf = Unpooled.copiedBuffer(bytes);
			DatagramPacket packet = new DatagramPacket(buf,
					new InetSocketAddress(host, port));
			ctx.writeAndFlush(packet);
	}

	public void echo(ChannelHandlerContext ctx, DatagramPacket m, byte[] bytes)
	{
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		DatagramPacket packet = new DatagramPacket(buf,
				new InetSocketAddress(m.sender().getHostString(), m.sender().getPort()));
		ctx.writeAndFlush(packet);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket m)
			throws Exception {
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		ClientMessage clientMessage = ClientMessage.parseFrom(bytes);
		
		NetMessage gameMessage = new NetMessage(null, NetMessage.UDP, bytes, m
				.sender().getHostString(), m.sender().getPort(), ctx);
		log.debug("MessageReceived length" + bytes.length + " "
				+ new String(bytes));
		this.inbound.tell(gameMessage, null);

	}

}
