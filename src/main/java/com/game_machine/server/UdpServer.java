package com.game_machine.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.example.udt.util.UtilThreadFactory;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UdpServer implements Runnable {

	public static Level logLevel = Level.INFO;
	private static final Logger log = Logger.getLogger(UdpServer.class.getName());

	private static final ChannelGroup allChannels = new DefaultChannelGroup("server");

	private final String hostname;
	private final int port;
	private NioEventLoopGroup acceptGroup;
	private final UdpServerHandler handler;

	public UdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		log.setLevel(UdpServer.logLevel);
		this.handler = new UdpServerHandler();
	}
	
	public void run() {
		log.info("Starting UdpServer");
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(10);
		final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
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

	public void send(byte[] bytes, String host, int port) {
		handler.send(bytes, host, port);
	}

	public void shutdown() {
		ChannelGroupFuture f = allChannels.close();
		try {
			f.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		acceptGroup.shutdown();
		log.warning("Server stopped");
	}

}
