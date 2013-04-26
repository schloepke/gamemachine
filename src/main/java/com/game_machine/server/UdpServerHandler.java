package com.game_machine.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import akka.actor.ActorSelection;

import com.game_machine.messages.NetMessage;
import com.game_machine.systems.Base;

@Sharable
public class UdpServerHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private static final Logger log = Logger.getLogger(UdpServerHandler.class.getName());
	public ChannelHandlerContext ctx = null;
	private ActorSelection inbound;

	public UdpServerHandler() {
		log.setLevel(UdpServer.logLevel);
		this.inbound = Base.system.actorSelection("/user/inbound");
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final DatagramPacket m) {
		m.retain();

		byte[] bytes = new byte[m.data().readableBytes()];
		m.data().readBytes(bytes);
		
		NetMessage gameMessage = new NetMessage(NetMessage.UDP,NetMessage.ENCODING_PROTOBUF,bytes,m.remoteAddress().getHostName(), m.remoteAddress().getPort());
		log.fine("MessageReceived length" + bytes.length + " " + new String(bytes));
		this.inbound.tell(gameMessage, null);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("Server channel active");
		this.ctx = ctx;
	}

	public void send(byte[] bytes, String host, int port) {
		if (this.ctx.channel().isActive() == true) {
			ByteBuf buf = Unpooled.copiedBuffer(bytes);
			DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(host, port));
			this.ctx.channel().write(packet);
		} else {
			log.warning("Client disconnected from server " + this.ctx.channel().remoteAddress());
		}
		
	}
	
	public void flush() {
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.warning("UdpHandler channel flushed");
			}
		});
	}

}
