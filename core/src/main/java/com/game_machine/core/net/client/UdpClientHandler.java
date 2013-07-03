package com.game_machine.core.net.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log =  LoggerFactory.getLogger(UdpClientHandler.class.getName());


    public UdpClientHandler() {
        
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.info("ECHO active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable cause) {
        log.warn("close the connection when an exception is raised", cause);
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
        }
        ctx.write(msgs);
    }
}