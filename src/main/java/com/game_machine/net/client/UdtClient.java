/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.game_machine.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.Config;
import com.game_machine.NetMessage;
import com.game_machine.ProtobufMessages;
import com.google.protobuf.MessageLite;

/**
 * UDT Byte Stream Client
 * <p>
 * Sends one message when a connection is open and echoes back any received data
 * to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the
 * server.
 */
public class UdtClient {

	public static Level logLevel = Level.INFO;

	private static final Logger log = Logger.getLogger(UdtClient.class.getName());

	private final String host;
	private final int port;
	private NioEventLoopGroup connectGroup;
	private Bootstrap boot;
	private UdtClientHandler handler;
	public ClientCallable callable;
	private int messageEncoding;
	
	public UdtClient(int messageEncoding, final String host, final int port) {
		this.host = host;
		this.port = port;
		this.messageEncoding = messageEncoding;
		log.setLevel(Level.parse(Config.logLevel));
	}

	public void setCallback(ClientCallable callable) {
		this.callable = callable;
	}
	
	public void start() {
		final ThreadFactory connectFactory = new UtilThreadFactory("connect");
		connectGroup = new NioEventLoopGroup(1, connectFactory, NioUdtProvider.MESSAGE_PROVIDER);
		try {
			boot = new Bootstrap();
			handler = new UdtClientHandler(this);
			boot.group(connectGroup).channelFactory(NioUdtProvider.MESSAGE_CONNECTOR).handler(new ChannelInitializer<UdtChannel>() {
				@Override
				public void initChannel(final UdtChannel ch) throws Exception {
					ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), handler);
				}
			});

			ChannelFuture f;
			try {
				f = boot.connect(host, port).sync();
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			stop();
		}
	}

	public Boolean send(Object message) {
		String clientId = Integer.toString(this.hashCode());
		byte[] bytes;
		
		if (message instanceof String) {
			bytes = ((String) message).getBytes();
		} else {
			bytes = (byte[]) message;
		}
		if (messageEncoding == NetMessage.ENCODING_PROTOBUF) {
			return this.handler.send(MessageBuilder.encode(bytes,clientId).toByteArray());
		} else {
			return this.handler.send(bytes);
		}
	}
	
	public void stop() {
		connectGroup.shutdown();
		log.warning("UdtClient STOPPED");
	}

}
