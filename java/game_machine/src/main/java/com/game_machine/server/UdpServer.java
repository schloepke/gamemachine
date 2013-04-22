package com.game_machine.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.example.udt.util.UtilThreadFactory;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.messages.GameMessage;

public class UdpServer implements Runnable {

	public static Level logLevel = Level.INFO;
	private static final Logger log = Logger.getLogger(UdpServer.class.getName());

	static final ChannelGroup allChannels = new DefaultChannelGroup("server");

	public final String hostname;
	public final int port;
	private NioEventLoopGroup acceptGroup;
	private Bootstrap boot;
	private final UdpServerHandler handler = new UdpServerHandler();

	public UdpServer(final String hostname, final int port) {
		this.port = port;
		this.hostname = hostname;
		log.setLevel(UdpServer.logLevel);
	}
	
	public void run() {
		handler.setServer(this);
		final DefaultEventExecutorGroup executor = new DefaultEventExecutorGroup(10);
		final ThreadFactory acceptFactory = new UtilThreadFactory("accept");
		acceptGroup = new NioEventLoopGroup();

		boot = new Bootstrap();
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
			// future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			stop();
		}
	}

	public void sendMessage(GameMessage message) {
		if (handler.ctx.channel().isActive() == true) {
			ByteBuf buf = Unpooled.copiedBuffer(message.bytes);
			DatagramPacket packet = new DatagramPacket(buf, new InetSocketAddress(message.host, message.port));
			handler.ctx.channel().write(packet);
		} else {
			log.warning("Client disconnected from server " + handler.ctx.channel().isActive() + " " + handler.ctx.channel().isOpen() + " " + handler.ctx.channel().remoteAddress());
		}
		
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
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
