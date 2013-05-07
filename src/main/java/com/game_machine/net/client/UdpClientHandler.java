package com.game_machine.net.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.game_machine.Config;
import com.game_machine.NetMessage;

public class UdpClientHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private static final Logger log = Logger.getLogger(UdpClientHandler.class.getName());

	private ChannelHandlerContext ctx = null;
	private UdpClient client;

	public UdpClientHandler(UdpClient client) {
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
		
		ByteBuf bmsg = Unpooled.copiedBuffer(bytes);
		DatagramPacket packet = new DatagramPacket(bmsg, new InetSocketAddress(client.host, client.port));
		ctx.write(packet);
		return true;
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		this.ctx = ctx;
		if (this.client.getMessageEncoding() == NetMessage.ENCODING_PROTOBUF) {
			this.client.callable.apply(MessageBuilder.encode("READY", ""));
		} else {
			this.client.callable.apply("READY");
		}
	}

	public void messageReceived(final ChannelHandlerContext ctx, final DatagramPacket m) {
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

	public void stop() {
		this.ctx.flush().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				client.stop();
			}
		});
	}

}
