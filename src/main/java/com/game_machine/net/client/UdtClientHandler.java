package com.game_machine.net.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.Config;
import com.game_machine.NetMessage;

public class UdtClientHandler extends ChannelInboundMessageHandlerAdapter<UdtMessage> {

	private static final Logger log = Logger.getLogger(UdtClientHandler.class.getName());

	private ChannelHandlerContext ctx = null;
	private UdtClient client;

	public UdtClientHandler(UdtClient client) {
		this.client = client;
		log.setLevel(Level.parse(Config.logLevel));
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
		log.info("UDT client sent " + new String(bytes));
		return true;
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		log.info("UdtClient active " + NioUdtProvider.socketUDT(ctx.channel()).toStringOptions());
		this.ctx = ctx;
		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			this.client.callable.apply(MessageBuilder.encode("READY", ""));
		} else {
			this.client.callable.apply("READY");
		}

	}

	public void messageReceived(final ChannelHandlerContext ctx, final UdtMessage m) {
		log.info("UdtClient messageReceived");
		byte[] bytes = new byte[m.data().readableBytes()];
		m.data().readBytes(bytes);

		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			this.client.callable.apply(MessageBuilder.decode(bytes));
		} else {
			this.client.callable.apply(bytes);
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
		log.log(Level.WARNING, "close the connection when an exception is raised", cause);
		ctx.close();
	}

}
