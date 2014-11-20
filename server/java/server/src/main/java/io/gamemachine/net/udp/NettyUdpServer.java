package io.gamemachine.net.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NettyUdpServer implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NettyUdpServer.class);
	private static Thread serverThread;

	private static NettyUdpServer udpServer;

	private final String hostname;
	private final int port;

	private final NettyUdpServerHandler handler;

	public static NettyUdpServer getUdpServer() {
		return udpServer;
	}

	public static void start(String host, Integer port) {
		
		// Don't try to start an already running server
		if (udpServer != null) {
			return;
		}

		udpServer = new NettyUdpServer(host, port);
		serverThread = new Thread(udpServer);
		serverThread.start();
	}

	public static void stop() {
		logger.info("Stopping UDP server");
		// Don't try to stop a server that's not running
		if (udpServer == null) {
			return;
		}

	}

	public NettyUdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		this.handler = new NettyUdpServerHandler();
	}

	public void run() {
		logger.info("Starting UdpServer port=" + this.port + " hostname=" + this.hostname);
		Thread.currentThread().setName("udp-server");
		String os = System.getProperty("os.name").toLowerCase();
		logger.info("OS is "+os);
		if (os.startsWith("linux")) {
			logger.info("UDP using Epoll");
			runLinux();
		} else {
			runGeneric();
		}
	}

	private void runGeneric() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.channel(NioDatagramChannel.class);
			boot.group(group);
			boot.option(ChannelOption.SO_BROADCAST, false);
			boot.option(ChannelOption.SO_RCVBUF, 102400);
			boot.handler(new NettyUdpServerHandler());

			InetSocketAddress address = new InetSocketAddress(hostname, port);
			boot.bind(address).sync().channel().closeFuture().await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	private void runLinux() {
		int threadcount = 5;
		try {
			Bootstrap boot = new Bootstrap();
			boot.channel(EpollDatagramChannel.class);
			boot.group(new EpollEventLoopGroup(threadcount));
			boot.option(ChannelOption.SO_BROADCAST, false);
			boot.option(ChannelOption.SO_RCVBUF, 302400);
			boot.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			boot.option(EpollChannelOption.SO_REUSEPORT, true);
			boot.handler(new LoggingHandler(LogLevel.INFO));
			boot.handler(new NettyUdpServerHandler());

			for (int i = 0; i < threadcount; ++i) {
				boot.bind(hostname,port).sync().channel();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//group.shutdownGracefully();
		}
	}
	
	public void sendToClient(InetSocketAddress address, byte[] bytes, ChannelHandlerContext ctx) {
		handler.send(address, bytes, ctx);
	}

}
