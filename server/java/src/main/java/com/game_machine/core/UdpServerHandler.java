package com.game_machine.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;
import akka.actor.ActorSelection;

//@Sharable
public final class UdpServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	public static AtomicInteger countIn = new AtomicInteger();
	public static AtomicInteger countOut = new AtomicInteger();

	private static final Logger log = LoggerFactory
			.getLogger(UdpServerHandler.class);
	public ChannelHandlerContext ctx = null;
	private ActorSelection inbound;

	private HashMap<Integer, InetSocketAddress> clients = new HashMap<Integer, InetSocketAddress>();

	public UdpServerHandler() {
		this.inbound = ActorUtil.getSelectionByName("message_gateway");
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

	public void send(ByteBuf buf, String host, int port,
			ChannelHandlerContext ctx) {
		InetSocketAddress address;
		int hashCode = ctx.hashCode();

		if (clients.containsKey(hashCode)) {
			address = clients.get(hashCode);
		} else {
			address = new InetSocketAddress(host, port);
			clients.put(hashCode, address);
		}

		DatagramPacket packet = new DatagramPacket(buf, address);
		ctx.writeAndFlush(packet);
		countOut.incrementAndGet();
	}
	
	public void send(byte[] bytes, String host, int port,
			ChannelHandlerContext ctx) {
		InetSocketAddress address;
		int hashCode = ctx.hashCode();

		if (clients.containsKey(hashCode)) {
			address = clients.get(hashCode);
		} else {
			address = new InetSocketAddress(host, port);
			clients.put(hashCode, address);
		}

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		DatagramPacket packet = new DatagramPacket(buf, address);
		ctx.writeAndFlush(packet);
		countOut.incrementAndGet();
	}

	public void echo(ChannelHandlerContext ctx, DatagramPacket m, byte[] bytes) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(m
				.sender().getHostString(), m.sender().getPort()));
		ctx.writeAndFlush(packet);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket m)
			throws Exception {
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		NetMessage gameMessage = new NetMessage(NetMessage.UDP, m.sender()
				.getHostString(), m.sender().getPort(), ctx);
		gameMessage.bytes = bytes;
		log.debug("MessageReceived length" + bytes.length + " "
				+ new String(bytes));
		this.inbound.tell(gameMessage, null);
		countIn.incrementAndGet();

	}

}
