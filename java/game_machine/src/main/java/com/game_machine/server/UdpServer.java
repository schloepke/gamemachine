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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UdpServer {

	public static Level logLevel = Level.INFO;
	private static final Logger log = Logger.getLogger(UdtServer.class.getName());

	static final ChannelGroup allChannels = new DefaultChannelGroup("server");

	private final String hostname;
	private final int port;
	private NioEventLoopGroup acceptGroup;
	private Bootstrap boot;

	public UdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		log.setLevel(UdpServer.logLevel);
	}

	private void configure() {
		final UdpServerHandler handler = new UdpServerHandler();
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(10);
		final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
		acceptGroup = new NioEventLoopGroup(1, acceptFactory);

		boot = new Bootstrap();
		handler.setServer(this);
		boot.channel(NioDatagramChannel.class);
		boot.group(acceptGroup);
		boot.option(ChannelOption.SO_BROADCAST, false);
		// boot.option(ChannelOption.SO_RCVBUF, 65536);
		// boot.option(ChannelOption.SO_SNDBUF, 65536);
		boot.handler(new ChannelInitializer<NioDatagramChannel>() {
			@Override
			public void initChannel(final NioDatagramChannel ch) {
				ChannelPipeline p = ch.pipeline();
				p.addLast(executor, new LoggingHandler(LogLevel.INFO), handler);
			}
		});
	}

	private void run() {
		ChannelFuture future;
		try {
			future = boot.bind(hostname, port);
			allChannels.add(future.channel());
			future.sync();
			// future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			stop();
		}
	}

	public void start() {
		configure();
		run();
	}
	
	public void stop() {
		ChannelGroupFuture f = allChannels.close();
		try {
			f.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boot.shutdown();
		acceptGroup.shutdown();
		log.warning("Server stopped");
	}

}
