package com.game_machine.server;

import io.netty.buffer.MessageBuf;
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
	private int messageCount = 0;

	public UdpServerHandler() {
		log.setLevel(UdpServer.logLevel);
	}

	public void setServer(UdpServer server) {
		this.server = server;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx, final DatagramPacket m) {
		m.retain();
		messageCount++;
		
		byte[] bytes = new byte[m.data().readableBytes()];
		m.data().readBytes(bytes);
		Msg msg = null;
		try {
			msg = Msg.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
		}
		String str = msg.getBody().toStringUtf8();
		
		//log.info("SERVER messageReceived " + str + " " + messageCount);

		if (str.equals("QUIT")) {
			log.warning("QUIT RECEVIED FROM CLIENT");
			ctx.flush();
			//stop();
		} else {
			if (Router.isRunning()) {
				Router.incoming.tell("TEST", Router.incoming);
			}
			
			
			if (ctx.channel().isActive() != false) {
				DatagramPacket packet = new DatagramPacket(m.data(), m.remoteAddress());
				final MessageBuf<Object> out = ctx.nextOutboundMessageBuffer();
				out.add(packet);
				//ctx.write(packet);
			} else {
				log.warning("Client disconnected from server " + ctx.channel().isActive() + " " + ctx.channel().isOpen() + " " + ctx.channel().remoteAddress());
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
		this.ctx = ctx;
	}

	public void beforeAdd(ChannelHandlerContext ctx) {
		log.warning("beforeAdd Context added");
		this.ctx = ctx;
	}

	public void stop() {
		if (this.ctx == null) {
			log.warning("Null Context!");
			server.stop();
			return;
		}
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				server.stop();
			}
		});
	}

}
