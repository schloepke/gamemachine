package com.game_machine.net.server;

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
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.Config;
import com.game_machine.NetMessage;

public final class UdpServer implements Runnable {

	public static Level logLevel = Level.INFO;
	private static final Logger log = Logger.getLogger(UdpServer.class.getName());
	private static final ChannelGroup allChannels = new DefaultChannelGroup("server");
	
	private static UdpServer udpServer;
	
	private final String hostname;
	private final int port;
	private NioEventLoopGroup acceptGroup;
	
	public static final int ENCODING_NONE = NetMessage.ENCODING_NONE;
	public static final int ENCODING_PROTOBUF = NetMessage.ENCODING_PROTOBUF;
	private final UdpServerHandler handler;

	
	public static UdpServer getUdpServer() {
		return udpServer;
	}
	
	public static void start(int messageEncoding) {

		// Don't try to start an already running server
		if (udpServer != null) {
			return;
		}

		UdpServer.logLevel = Level.parse(Config.logLevel);
		udpServer = new UdpServer(messageEncoding, Config.udpHost, Config.udpPort);
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

	public UdpServer(final int messageEncoding, final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		log.setLevel(Level.parse(Config.logLevel));
		this.handler = new UdpServerHandler(messageEncoding);
	}

	public void run() {
		log.warning("Starting UdpServer port=" + this.port + " hostname=" + this.hostname);
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
		if (acceptGroup != null) {
			acceptGroup.shutdown();
		}
	}

}
