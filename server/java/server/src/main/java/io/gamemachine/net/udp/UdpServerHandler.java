package io.gamemachine.net.udp;

import io.gamemachine.config.GameLimits;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.NetMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.routing.Incoming;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.ChannelHandler.Sharable;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;

@Sharable
public final class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);
	public ChannelHandlerContext ctx = null;
	private ActorSelection inbound;

	public UdpServerHandler() {
		this.inbound = ActorUtil.getSelectionByName(Incoming.name);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		logger.info("close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		logger.debug("UDP channel active");
		this.ctx = ctx;
	}

	public void send(InetSocketAddress address, byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.wrappedBuffer(bytes);
		DatagramPacket packet = new DatagramPacket(buf, address);
		ctx.writeAndFlush(packet);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket m) throws Exception {
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);

		ClientMessage clientMessage = ClientMessage.parseFrom(bytes);
		NetMessage netMessage = new NetMessage(NetMessage.UDP, m.sender().getHostString(), m.sender().getPort(), ctx);
		netMessage.clientMessage = clientMessage;
		netMessage.address = m.sender();

//		if (clientMessage.hasSentAt()) {
//			long latency = System.currentTimeMillis() - clientMessage.getSentAt();
//			if (latency >= 4) {
//				logger.info("ClientMessage latency " + latency);
//			}
//		}

		logger.debug("MessageReceived length" + bytes.length + " " + new String(bytes));
		this.inbound.tell(netMessage, null);

	}

}
