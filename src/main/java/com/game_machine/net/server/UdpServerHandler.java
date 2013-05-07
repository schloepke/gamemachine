package com.game_machine.net.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import akka.actor.ActorSelection;

import com.game_machine.ActorUtil;
import com.game_machine.Config;
import com.game_machine.NetMessage;
import com.game_machine.game.Gateway;

//@Sharable
public final class UdpServerHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private static final Logger log = LoggerFactory.getLogger(UdpServerHandler.class);
	public ChannelHandlerContext ctx = null;
	private ActorSelection inbound;
	private int messageEncoding;
	
	public UdpServerHandler(int messageEncoding) {
		this.inbound = ActorUtil.getSelectionByClass(Gateway.class);
		this.messageEncoding = messageEncoding;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final DatagramPacket m) {
		m.retain();
		
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);
		
		NetMessage gameMessage = new NetMessage(null,NetMessage.UDP,messageEncoding,bytes,m.sender().getHostName(), m.sender().getPort(),null);
		log.debug("MessageReceived length" + bytes.length + " " + new String(bytes));
		this.inbound.tell(gameMessage, null);
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.info("close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("UDP server active");
		this.ctx = ctx;
	}

	public void send(byte[] bytes, String host, int port, ChannelHandlerContext ctx) {
		if (this.ctx.channel().isActive() == true) {
			ByteBuf buf = Unpooled.copiedBuffer(bytes);
			DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(host, port));
			this.ctx.channel().write(packet);
		} else {
			log.info("Client disconnected from server " + this.ctx.channel().remoteAddress());
		}
		
	}
	
	public void flush() {
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.info("UdpHandler channel flushed");
			}
		});
	}

}
