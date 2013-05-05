package com.game_machine.net.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UdtClientHandler extends ChannelInboundMessageHandlerAdapter<UdtMessage> {

	private static final Logger log = Logger.getLogger(UdtClientHandler.class.getName());

	private ChannelHandlerContext ctx = null;
	private UdtClient client;

	public UdtClientHandler(UdtClient client) {
		this.client = client;
		log.setLevel(UdtClient.logLevel);
	}

	public Boolean send(byte[] bytes) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);
		this.ctx.channel().write(message);
		this.ctx.flush();
		log.fine("UDT client sent "+ new String(bytes));
		return true;
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.fine("UdpClient active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
		this.ctx = ctx;
		this.client.callable.apply("READY".getBytes());

	}

	public void messageReceived(final ChannelHandlerContext ctx, final UdtMessage m) {
		log.fine("UdtClient messageReceived");
		byte[] bytes = new byte[m.data().readableBytes()];
		m.data().readBytes(bytes);
		this.client.callable.apply(bytes);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

}
