package com.game_machine.core;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;

public class TcpServerInitializer  extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;
	private TcpServerHandler tcpServerHandler;
	
    public TcpServerInitializer(TcpServerHandler handler, SslContext sslCtx) {
        this.sslCtx = sslCtx;
        this.tcpServerHandler = handler;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ClientMessageDecoder());

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());

        p.addLast(tcpServerHandler);
    }
}
