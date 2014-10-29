package com.game_machine.net.tcp;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import GameMachine.Messages.ClientMessage;

public class ClientMessageEncoder extends MessageToMessageEncoder<ClientMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ClientMessage msg, List<Object> out) throws Exception {
		if (msg instanceof ClientMessage) {
			out.add(wrappedBuffer(msg.toPrefixedByteArray()));
			return;
		}
	}
}
