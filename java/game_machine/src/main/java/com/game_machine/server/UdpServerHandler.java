package com.game_machine.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.messages.ClientMessage.Msg;
import com.google.protobuf.InvalidProtocolBufferException;

@Sharable
public class UdpServerHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private static final Logger log = Logger.getLogger(UdpServerHandler.class.getName());
	private UdpServer server;
	private ChannelHandlerContext ctx = null;

	public UdpServerHandler() {
		log.setLevel(UdpServer.logLevel);
	}

	public void setServer(UdpServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final DatagramPacket m) {
		byte[] bytes = new byte[m.data().readableBytes()];
		m.data().readBytes(bytes);
		Msg msg = null;
		try {
			msg = Msg.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
		}
		String str = msg.getBody().toStringUtf8();
		
		log.info("SERVER messageReceived " + str);

		if (str.equals("QUIT")) {
			log.warning("QUIT RECEVIED FROM CLIENT");
			stop();
		} else {
			if (Router.isRunning()) {
				Router.incoming.tell("TEST", Router.incoming);
			}
			DatagramPacket packet = new DatagramPacket(m.data(), m.remoteAddress());
			
			if (ctx.channel().remoteAddress() != null) {
				log.warning(ctx.channel().remoteAddress().toString());
				ctx.write(packet);
			}
		}
	}

	public void sendMessage(String message) {

	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("SERVER ECHO active ");
	}

	public void beforeAdd(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public void stop() {
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				server.stop();
			}
		});
	}

}
