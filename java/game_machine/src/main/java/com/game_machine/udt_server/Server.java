package com.game_machine.udt_server;

import io.netty.bootstrap.ChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.example.udt.util.UtilThreadFactory;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.*;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

/**
 * UDT Byte Stream Server
 * <p>
 * Echoes back any received data from a client.
 */
public class Server implements Runnable {

	private static final Logger log = Logger.getLogger(Server.class.getName());

	private final int port;
	private NioEventLoopGroup acceptGroup;
	private NioEventLoopGroup connectGroup;

	public Server(final int port) {
		this.port = port;
	}

	public void run() {
		log.info("Server Starting");
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(5);
		final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
		final ThreadFactory connectFactory = new UtilThreadFactory("connect");
		acceptGroup = new NioEventLoopGroup(1, acceptFactory, NioUdtProvider.BYTE_PROVIDER);
		connectGroup = new NioEventLoopGroup(1, connectFactory, NioUdtProvider.BYTE_PROVIDER);
		
		// Configure the server.
		try {
			final ServerBootstrap boot = new ServerBootstrap();
			final ServerHandler handler = new ServerHandler(this);
			boot.group(acceptGroup, connectGroup).channelFactory(NioUdtProvider.BYTE_ACCEPTOR);
			boot.option(ChannelOption.SO_BACKLOG, 10);
			boot.handler(new LoggingHandler(LogLevel.INFO));

			boot.childHandler(new ChannelInitializer<UdtChannel>() {
				@Override
				public void initChannel(final UdtChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
			        p.addLast("protobufDecoder", new ProtobufDecoder(GameProtocol.Msg.getDefaultInstance()));

			        p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
			        p.addLast("protobufEncoder", new ProtobufEncoder());
			        
					p.addLast(executor, new LoggingHandler(LogLevel.INFO), handler);
				}
			});
			
			// Start the server.
			ChannelFuture future;
			try {
				future = boot.bind(port).sync();
				future.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
				stop();
			}
		} finally {
			// Shut down all event loops to terminate all threads.
			stop();
		}
	}

	public void stop() {
		acceptGroup.shutdown();
		connectGroup.shutdown();
		log.info("Server stopped");
	}

	public static Thread start(int port) throws Exception {
		Thread t = new Thread(new Server(port));
		t.start();
		return t;
	}

}
