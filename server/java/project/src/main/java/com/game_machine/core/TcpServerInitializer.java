package com.game_machine.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.ssl.SslContext;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;

	public TcpServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}

		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(new ClientMessageDecoder());

		// p.addLast(new ProtobufVarint32LengthFieldPrepender());
		p.addLast(new ClientMessageEncoder());

		p.addLast(new TcpServerHandler());
	}
}
