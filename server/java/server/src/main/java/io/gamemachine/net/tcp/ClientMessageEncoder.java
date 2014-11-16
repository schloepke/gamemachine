package io.gamemachine.net.tcp;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.gamemachine.config.GameLimits;
import io.gamemachine.messages.ClientMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ClientMessageEncoder extends MessageToMessageEncoder<ClientMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ClientMessage msg, List<Object> out) throws Exception {
		if (msg instanceof ClientMessage) {
			
			// It has a game id if it's a normal client and not an agent
			if (msg.hasGameId()) {
				String gameId = msg.getGameId();
				msg.setGameId(null);
				byte[] bytes = msg.toPrefixedByteArray();
				GameLimits.addBytesTransferred(gameId, bytes.length);
				out.add(wrappedBuffer(bytes));
			} else {
				out.add(wrappedBuffer(msg.toPrefixedByteArray()));
			}
			return;
		}
	}
}
