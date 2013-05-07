package com.game_machine.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.Config;


public class UdpClient {


	private static final Logger log = LoggerFactory.getLogger(UdpClient.class);

	public final String host;
	public final int port;
	private NioEventLoopGroup connectGroup;
	private Bootstrap boot;
	private UdpClientHandler handler;
	public ClientCallable callable;
	private int messageEncoding;

	public UdpClient(int messageEncoding, final String host, final int port) {
		this.host = host;
		this.port = port;
		this.messageEncoding = messageEncoding;
	}

	public void setCallback(ClientCallable callable) {
		this.callable = callable;
	}
	
	public int getMessageEncoding() {
		return this.messageEncoding;
	}
	
	public void start() {
		final ThreadFactory connectFactory = new UtilThreadFactory("connect");
		connectGroup = new NioEventLoopGroup(5, connectFactory);
		try {
			boot = new Bootstrap();
			handler = new UdpClientHandler(this);
			boot.group(connectGroup);
			boot.channel(NioDatagramChannel.class);
			boot.option(ChannelOption.SO_BROADCAST, false);
			//boot.option(ChannelOption.SO_RCVBUF, 65536);
			//boot.option(ChannelOption.SO_SNDBUF, 65536);
			boot.handler(new ChannelInitializer<NioDatagramChannel>() {
				@Override
				public void initChannel(final NioDatagramChannel ch) {
					ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), handler);
				}
			});

			ChannelFuture f;
			try {
				f = boot.bind(0).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			stop();
		}
	}
	
	public Boolean send(Object message) {
		return this.handler.send(message);
	}
	
	
	public void stop() {
		connectGroup.shutdownGracefully();
		log.info("UdpClient STOPPED");
	}

}
