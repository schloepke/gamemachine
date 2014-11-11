package io.gamemachine.net.tcp;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.NetMessage;
import io.gamemachine.routing.Incoming;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;
import akka.actor.ActorSelection;

public class TcpServerHandler extends SimpleChannelInboundHandler<ClientMessage> {

	private ActorSelection inbound;

	private static final Logger log = LoggerFactory.getLogger(TcpServerHandler.class);

	public TcpServerHandler() {
		this.inbound = ActorUtil.getSelectionByName(Incoming.name);
	}

	public static void sendClientMessage(ClientMessage clientMessage, ChannelHandlerContext ctx) {
		ctx.write(clientMessage);
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, ClientMessage clientMessage) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

		NetMessage netMessage = new NetMessage(NetMessage.TCP, address.getHostString(), address.getPort(), ctx);
		netMessage.clientMessage = clientMessage;
		this.inbound.tell(netMessage, null);
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("Tcp server active");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// cause.printStackTrace();
		ctx.close();
	}

}
