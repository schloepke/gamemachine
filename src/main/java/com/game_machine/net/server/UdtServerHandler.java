package com.game_machine.net.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import akka.actor.ActorSelection;

import com.game_machine.ActorUtil;
import com.game_machine.NetMessage;
import com.game_machine.game.Gateway;

@Sharable
public class UdtServerHandler extends ChannelInboundMessageHandlerAdapter<UdtMessage> {

	private static final Logger log = LoggerFactory.getLogger(UdtServerHandler.class);
	private UdtServer server;
	private ActorSelection inbound;
	private Integer messageEncoding;

	public UdtServerHandler(int messageEncoding) {
		this.inbound = ActorUtil.getSelectionByClass(Gateway.class);
		this.messageEncoding = messageEncoding;
	}

	public void setServer(UdtServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final UdtMessage m) {
		m.retain();

		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		log.debug("UDT server got " + new String(bytes));
		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		NetMessage gameMessage = new NetMessage(null,NetMessage.UDT, messageEncoding, bytes, host, port,ctx);
		this.inbound.tell(gameMessage, null);
	}

	public void sendToClient(byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);
		ctx.channel().write(message);
		log.debug("UDT server sent " + new String(bytes));
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.debug("close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.debug("UDT server active "+ NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
	}

}
