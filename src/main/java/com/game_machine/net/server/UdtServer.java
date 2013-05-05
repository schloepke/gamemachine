package com.game_machine.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.*;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import akka.actor.ActorSystem;

import com.game_machine.Config;
import com.game_machine.ProtobufMessages;
import com.game_machine.net.client.UtilThreadFactory;

public class UdtServer implements Runnable {

	public static Level logLevel = Level.INFO;
	private static final Logger log = Logger.getLogger(UdtServer.class.getName());

	static final ChannelGroup allChannels = new DefaultChannelGroup("server");
	
	private static UdtServer udtServer;
	private final String hostname;
	private final int port;
	private NioEventLoopGroup acceptGroup;
	private NioEventLoopGroup connectGroup;
	private ServerBootstrap boot;
	private UdtServerHandler handler;

	public UdtServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		log.setLevel(UdtServer.logLevel);
		handler = new UdtServerHandler();
	}

	public void run() {
		log.warning("Starting UdtServer port=" + this.port + " hostname=" + this.hostname);
		Thread.currentThread().setName("udt-server");
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(10);
		final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
		final ThreadFactory connectFactory = new UtilThreadFactory("connect");
		acceptGroup = new NioEventLoopGroup(1, acceptFactory, NioUdtProvider.BYTE_PROVIDER);
		connectGroup = new NioEventLoopGroup(1, connectFactory, NioUdtProvider.BYTE_PROVIDER);
		
		// Configure the server.
		try {
			boot = new ServerBootstrap();
			handler.setServer(this);
			boot.group(acceptGroup, connectGroup).channelFactory(NioUdtProvider.BYTE_ACCEPTOR);
			boot.option(ChannelOption.SO_BACKLOG, 10);
			boot.handler(new LoggingHandler(LogLevel.INFO));

			boot.childHandler(new ChannelInitializer<UdtChannel>() {
				@Override
				public void initChannel(final UdtChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
			        p.addLast("protobufDecoder", new ProtobufDecoder(ProtobufMessages.ClientMessage.getDefaultInstance()));

			        p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
			        p.addLast("protobufEncoder", new ProtobufEncoder());
			        
					p.addLast(executor, new LoggingHandler(LogLevel.INFO), handler);
				}
			});
			
			// Start the server.
			ChannelFuture future;
			try {
				future = boot.bind(hostname,port);
				allChannels.add(future.channel());
				future.sync();
				
				//future.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
				shutdown();
			}
		} finally {
		}
	}

	public void send(byte[] bytes, String host, int port) {
		handler.send(bytes, host, port);
	}
	
	public static UdtServer getUdtServer() {
		return udtServer;
	}
	
	public static void start() {

		// Don't try to start an already running server
		if (udtServer != null) {
			return;
		}

		UdtServer.logLevel = Level.parse(Config.logLevel);
		udtServer = new UdtServer(Config.udtHost, Config.udtPort);
		new Thread(udtServer).start();
	}
	
	public static void stop() {

		// Don't try to stop a server that's not running
		if (udtServer == null) {
			return;
		}

		udtServer.shutdown();
		udtServer = null;
	}
	
	public void shutdown() {
		ChannelGroupFuture f = allChannels.close();
        try {
			f.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		acceptGroup.shutdown();
		connectGroup.shutdown();
		log.warning("Server stopped");
	}


}
