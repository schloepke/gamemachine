package com.game_machine.core.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ThreadFactory;

public class UdtClient implements Runnable {

    private final int port;
    private final String address;
    private UdtClientHandler handler;
    private static Thread clientThread;
	public static UdtClient udtClient;
	private NioEventLoopGroup connectGroup;
	
	
    public UdtClient(String name, String address, int port) {
    	this.address = address;
        this.port = port;
        handler = new UdtClientHandler(name);
    }

    public void sendToServer(byte[] bytes, ChannelHandlerContext ctx) {
		handler.send(bytes, ctx);
	}
    
    public static void start(String name, String host, Integer port) {

		// Don't try to start an already running server
		if (udtClient != null) {
			return;
		}

		udtClient = new UdtClient(name, host, port);
		clientThread = new Thread(udtClient);
		clientThread.start();
	}
    
    public void shutdown() {
    	clientThread.stop();
		connectGroup.shutdownGracefully();
	}
    
    public void run() {
        // Configure the client.
        final ThreadFactory connectFactory = new UtilThreadFactory("connect");
        connectGroup = new NioEventLoopGroup(1,
                connectFactory, NioUdtProvider.MESSAGE_PROVIDER);
        try {
            final Bootstrap boot = new Bootstrap();
            boot.group(connectGroup)
                    .channelFactory(NioUdtProvider.MESSAGE_CONNECTOR)
                    .handler(new ChannelInitializer<UdtChannel>() {
                        @Override
                        public void initChannel(final UdtChannel ch) throws Exception {
                            ch.pipeline().addLast(handler);
                        }
                    });
            // Start the client.
            final ChannelFuture f = boot.connect(address, port).sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            // Shut down the event loop to terminate all threads.
            connectGroup.shutdownGracefully();
        }
    }


}