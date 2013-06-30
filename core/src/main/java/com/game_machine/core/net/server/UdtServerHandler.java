package com.game_machine.core.net.server;

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

import com.game_machine.core.ActorUtil;
import com.game_machine.core.NetMessage;

@Sharable
public class UdtServerHandler extends ChannelInboundMessageHandlerAdapter<UdtMessage> {

	private static final Logger log = LoggerFactory.getLogger(UdtServerHandler.class);
	private UdtServer server;
	private ActorSelection inbound;

	public UdtServerHandler() {
		this.inbound = ActorUtil.getSelectionByName("GameMachine::Protocols::Udt");
	}

	public void setServer(UdtServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final UdtMessage m) {
		//m.retain();

		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		log.debug("UDT server got " + new String(bytes));
		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
		log.debug("UDT RemoteHost:" + host + " RemotePort:" + port);
		NetMessage gameMessage = new NetMessage(null,NetMessage.UDT, bytes, host, port,ctx);
		this.inbound.tell(gameMessage, null);
	}

	public void send(byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);
		ctx.channel().write(message);
		log.debug("UDT server sent " + new String(bytes) + " " + bytes.length);
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
