package com.game_machine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.game_machine.messages.ProtobufMessages.ClientMsg;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

public class MessageUtil {

	public static ByteBuf messageToByteBuf(MessageLite msg) {
		return Unpooled.copiedBuffer(msg.toByteArray());
	}
	
	public static ClientMsg buildClientMsg(String str, String hostname) {
		ClientMsg.Builder builder = ClientMsg.newBuilder();
		ByteString reply = ByteString.copyFromUtf8(str);
		builder.setBody(reply);
		builder.setHostname(hostname);
		ClientMsg msg = builder.build();
		return msg;
	}
	
}
