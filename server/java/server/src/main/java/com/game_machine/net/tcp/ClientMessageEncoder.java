package com.game_machine.net.tcp;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import GameMachine.Messages.ClientMessage;

import com.game_machine.config.GameLimits;

public class ClientMessageEncoder extends MessageToMessageEncoder<ClientMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ClientMessage msg, List<Object> out) throws Exception {
		if (msg instanceof ClientMessage) {
			String gameId = msg.getGameId();
			msg.setGameId(null);
			byte[] bytes = msg.toPrefixedByteArray();
			GameLimits.addBytesTransferred(gameId, bytes.length);
			out.add(wrappedBuffer(bytes));
			return;
		}
	}
}
