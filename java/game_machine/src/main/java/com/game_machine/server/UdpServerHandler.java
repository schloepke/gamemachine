package com.game_machine.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.logging.Level;
import java.util.logging.Logger;

import akka.actor.ActorSelection;

import com.game_machine.messages.GameMessage;
import com.game_machine.systems.Root;

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
		
		GameMessage gameMessage = new GameMessage(bytes,m.remoteAddress().getHostName(),m.remoteAddress().getPort());
		log.info("MessageReceived length" + bytes.length);
		ActorSelection ref = Root.system.actorSelection("/user/inbound");
		ref.tell(gameMessage);
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
		Server.udpServer = server;
		ActorSelection ref = Root.system.actorSelection("/user/outbound");
		//ref.tell(server);
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
