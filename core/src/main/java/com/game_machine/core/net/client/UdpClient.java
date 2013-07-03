package com.game_machine.core.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * A UDP broadcast client that asks for a quote of the moment (QOTM) to
 * {@link QuoteOfTheMomentServer}.
 *
 * Inspired by <a href="http://goo.gl/BsXVR">the official Java tutorial</a>.
 */
public class UdpClient {

    private final int port;
    private final String address;
    private UdpClientHandler handler;

    public UdpClient(String address, int port) {
    	this.address = address;
        this.port = port;
        handler = new UdpClientHandler();
    }

    public void run() throws Exception {
        // Configure the client.
        final ThreadFactory connectFactory = new UtilThreadFactory("connect");
        final NioEventLoopGroup connectGroup = new NioEventLoopGroup(1,
                connectFactory, NioUdtProvider.MESSAGE_PROVIDER);
        try {
            final Bootstrap boot = new Bootstrap();
            boot.group(connectGroup)
                    .channelFactory(NioUdtProvider.MESSAGE_CONNECTOR)
                    .handler(new ChannelInitializer<UdtChannel>() {
                        @Override
                        public void initChannel(final UdtChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), handler);
                        }
                    });
            // Start the client.
            final ChannelFuture f = boot.connect(address, port).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            connectGroup.shutdownGracefully();
        }
    }


}