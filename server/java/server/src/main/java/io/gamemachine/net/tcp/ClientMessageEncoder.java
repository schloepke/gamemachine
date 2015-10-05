package io.gamemachine.net.tcp;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.gamemachine.config.GameLimits;
import io.gamemachine.messages.ClientMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.google.common.base.Strings;

public class ClientMessageEncoder extends MessageToMessageEncoder<ClientMessage> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ClientMessage msg, List<Object> out) throws Exception {
		if (msg instanceof ClientMessage) {
			String playerId;
			// It has a game id if it's a normal client and not an agent
			if (Strings.isNullOrEmpty(msg.gameId)) {
				if (msg.player != null) {
					playerId = msg.player.id;
				} else {
					playerId = "nobody";
				}
				String gameId = msg.getGameId();
				msg.setGameId(null);
				byte[] bytes = msg.toPrefixedByteArray();
				GameLimits.addBytesTransferred(gameId, playerId,bytes.length);
				out.add(wrappedBuffer(bytes));
			} else {
				out.add(wrappedBuffer(msg.toPrefixedByteArray()));
			}
			return;
		}
	}
}
