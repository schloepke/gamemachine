package com.game_machine.net.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;


import com.game_machine.Config;
import com.game_machine.NetMessage;

public class UdtClientHandler extends ChannelInboundMessageHandlerAdapter<UdtMessage> {

	private static final Logger log = LoggerFactory.getLogger(UdtClientHandler.class);

	private ChannelHandlerContext ctx = null;
	private UdtClient client;

	public UdtClientHandler(UdtClient client) {
		this.client = client;
	}

	public Boolean send(Object message) {
		byte[] bytes;

		if (message instanceof String) {
			bytes = ((String) message).getBytes();
		} else {
			bytes = (byte[]) message;
		}
		
		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			String clientId = Integer.toString(this.hashCode());
			bytes = MessageBuilder.encode(bytes, clientId).toByteArray();
		}

		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		UdtMessage udtMessage = new UdtMessage(buf);
		this.ctx.channel().write(udtMessage);
		this.ctx.flush();
		log.debug("UDT client sent " + new String(bytes));
		return true;
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.debug("UdtClient active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
		this.ctx = ctx;
		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			this.client.callable.apply(MessageBuilder.encode("READY", ""));
		} else {
			this.client.callable.apply("READY");
		}

	}

	public void messageReceived(final ChannelHandlerContext ctx, final UdtMessage m) {
		log.debug("UdtClient messageReceived");
		
		byte[] bytes = new byte[m.content().readableBytes()];
		m.content().readBytes(bytes);
		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			this.client.callable.apply(MessageBuilder.decode(bytes));
		} else {
			this.client.callable.apply(bytes);
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.info("close the connection when an exception is raised", cause);
		ctx.close();
	}

}
