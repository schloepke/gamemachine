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

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.udt.nio.SelectorProviderUDT;
import com.game_machine.core.net.server.UdtServer;

public class UdtClient implements Runnable {

    private final int port;
    private final String address;
    private UdtClientHandler handler;
    public Thread clientThread;
	private NioEventLoopGroup connectGroup;
	private static final Logger log = LoggerFactory.getLogger(UdtClient.class);
	
    public UdtClient(String name, String address, int port) {
    	this.address = address;
        this.port = port;
        handler = new UdtClientHandler(name);
    }

    public void sendToServer(byte[] bytes, ChannelHandlerContext ctx) {
		handler.send(bytes, ctx);
	}
    
    public static UdtClient start(String name, String host, Integer port) {
    	log.info("UdtClient start");
		UdtClient udtClient = new UdtClient(name, host, port);
		udtClient.clientThread = new Thread(udtClient);
		udtClient.clientThread.start();
		return udtClient;
	}
    
    public void shutdown() {
    	clientThread.stop();
		connectGroup.shutdownGracefully();
	}
    
    public void run() {
    	log.info("UdtClient run");
        // Configure the client.
    	SelectorProviderUDT provider = (SelectorProviderUDT) NioUdtProvider.MESSAGE_PROVIDER;
    	provider.setMaxSelectorSize(10000);
        final ThreadFactory connectFactory = new UtilThreadFactory("connect");
        connectGroup = new NioEventLoopGroup(1,
                connectFactory, provider);
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
            // Start the client
            log.info("UDT Client connecting to " + address + ":" + port);
        
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