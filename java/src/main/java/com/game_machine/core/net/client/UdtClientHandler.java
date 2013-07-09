package com.game_machine.core.net.client;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMachineLoader;
import com.game_machine.core.NetMessage;

import akka.actor.ActorSelection;

public class UdtClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log =  LoggerFactory.getLogger(UdtClientHandler.class.getName());
    private ActorSelection inbound;

    public UdtClientHandler(String name) {
    	this.inbound = ActorUtil.getSelectionByName(name);
    }

    public void send(byte[] bytes, ChannelHandlerContext ctx) {

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage message = new UdtMessage(buf);
		
		ctx.channel().write(message);
		log.debug("UDT server sent " + new String(bytes) + " " + bytes.length);
		//buf.release();
	}
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.debug("ECHO active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
        this.inbound.tell(ctx,null);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable cause) {
        //log.warn("close the connection when an exception is raised", cause);
        ctx.close();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageList<Object> msgs) throws Exception {
        MessageList<UdtMessage> buffers = msgs.cast();

        for (int i = 0; i < buffers.size(); i++) {
            UdtMessage m = buffers.get(i);
            byte[] bytes = new byte[m.content().readableBytes()];
    		m.content().readBytes(bytes);
    		m.content().release();
    		
    		log.debug("UDT server got " + bytes);
    		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
    		int port = ((InetSocketAddress) ctx.channel().remoteAddress()).getPort();
    		log.debug("UDT RemoteHost:" + host + " RemotePort:" + port);
    		NetMessage gameMessage = new NetMessage(null,NetMessage.UDT, bytes, host, port,ctx);
    		this.inbound.tell(gameMessage, null);
        }
    }
}