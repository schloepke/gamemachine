package com.game_machine.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.actors.Master;
import com.game_machine.messages.ProtobufMessages.ClientMsg;
import com.google.protobuf.InvalidProtocolBufferException;

@Sharable
public class UdpServerHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private static final Logger log = Logger.getLogger(UdpServerHandler.class.getName());
	private UdpServer server;
	public ChannelHandlerContext ctx = null;
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
		ClientMsg msg = null;
		try {
			msg = ClientMsg.parseFrom(bytes);
			ClientMsg.Builder builder = msg.toBuilder();
			builder.setHostname(m.remoteAddress().getHostName());
			builder.setPort(m.remoteAddress().getPort());
			msg = builder.build();
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
			return;
		}
		String str = msg.getBody().toStringUtf8();

		// log.info("SERVER messageReceived " + str + " " + messageCount);

		if (Master.isRunning()) {
			Master.router.tell(msg, Master.router);
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
		log.info("Server channel active");
		this.ctx = ctx;
		Master.router.tell((GameProtocolServer) server, Master.router);
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
