package com.game_machine.core.net.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;


import com.game_machine.core.Config;
import com.game_machine.core.NetMessage;

public final class UdpServer implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UdpServer.class);
	private static final ChannelGroup allChannels = new DefaultChannelGroup("server");
	
	private static UdpServer udpServer;
	
	private final String hostname;
	private final int port;
	private NioEventLoopGroup acceptGroup;
	
	private final UdpServerHandler handler;

	
	public static UdpServer getUdpServer() {
		return udpServer;
	}
	
	public static void start() {

		// Don't try to start an already running server
		if (udpServer != null) {
			return;
		}

		udpServer = new UdpServer(Config.udpHost, Config.udpPort);
		new Thread(udpServer).start();
	}

	public static void stop() {

		// Don't try to stop a server that's not running
		if (udpServer == null) {
			return;
		}

		udpServer.shutdown();
		udpServer = null;
	}

	public UdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		this.handler = new UdpServerHandler();
	}

	public void run() {
		log.info("Starting UdpServer port=" + this.port + " hostname=" + this.hostname);
		Thread.currentThread().setName("udp-server");
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(10);
		// final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
		acceptGroup = new NioEventLoopGroup();

		Bootstrap boot = new Bootstrap();
		boot.channel(NioDatagramChannel.class);
		boot.group(acceptGroup);
		boot.option(ChannelOption.SO_BROADCAST, false);
		boot.handler(new ChannelInitializer<NioDatagramChannel>() {
			@Override
			public void initChannel(final NioDatagramChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(executor, "handler", handler);
			}
		});

		ChannelFuture future;
		try {
			future = boot.bind(hostname, port);
			allChannels.add(future.channel());
			future.sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			shutdown();
		}
	}

	public void send(byte[] bytes, String host, int port, ChannelHandlerContext ctx) {
		handler.send(bytes, host, port, ctx);
	}

	public void shutdown() {
		ChannelGroupFuture f = allChannels.close();
		try {
			f.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (acceptGroup != null) {
			acceptGroup.shutdownGracefully();
		}
	}

}
