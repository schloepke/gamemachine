package com.game_machine.core.net.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.NetMessage;

public class UdtClientHandler extends SimpleChannelInboundHandler<UdtMessage> {

	private static final Logger log = LoggerFactory
			.getLogger(UdtClientHandler.class.getName());
	private ActorSelection inbound;

	public UdtClientHandler(String name) {
		this.inbound = ActorUtil.getSelectionByName(name);
	}

	public void send(byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);

		ctx.channel().write(message);
		ctx.channel().flush();
		log.debug("UDT client sent " + new String(bytes) + " " + bytes.length);
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.debug("ECHO active "
				+ NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
		this.inbound.tell(ctx, null);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx,
			final Throwable cause) {
		log.warn("close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, UdtMessage m)
			throws Exception {
		log.debug("UdtClient channelRead0 " + m);
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		log.debug("UdtClient got " + bytes);
		String host = ((InetSocketAddress) ctx.channel().remoteAddress())
				.getAddress().getHostAddress();
		int port = ((InetSocketAddress) ctx.channel().remoteAddress())
				.getPort();
		log.debug("UDT RemoteHost:" + host + " RemotePort:" + port);
		NetMessage gameMessage = new NetMessage(null, NetMessage.UDT, bytes,
				host, port, ctx);
		this.inbound.tell(gameMessage, null);
	}
}