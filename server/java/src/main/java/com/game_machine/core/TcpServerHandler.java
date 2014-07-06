package com.game_machine.core;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ClientMessage;
import akka.actor.ActorSelection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpServerHandler extends SimpleChannelInboundHandler<ClientMessage> {
	
	private ActorSelection inbound;
	private ChannelHandlerContext ctx;
	
	private static final Logger log = LoggerFactory
			.getLogger(TcpServerHandler.class);
	
	public TcpServerHandler() {
		this.inbound = ActorUtil
				.getSelectionByName("message_gateway");
	}
	
	public void sendClientMessage(ClientMessage clientMessage) {
		ctx.write(clientMessage);
	}
	@Override
    public void channelRead0(ChannelHandlerContext ctx, ClientMessage clientMessage) throws Exception {
        
		InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
		
		NetMessage netMessage = new NetMessage(NetMessage.TCP, address.getHostString(), address.getPort(), ctx);
		netMessage.clientMessage = clientMessage;
		this.inbound.tell(netMessage, null);
        //ctx.write(builder.build());
    }

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		log.info("Tcp server active");
		this.ctx = ctx;
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
