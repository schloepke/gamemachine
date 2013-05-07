package com.game_machine;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.game_machine.ProtobufMessages.ClientMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;

public class MessageUtil {

	public static ByteBuf messageToByteBuf(MessageLite msg) {
		return Unpooled.copiedBuffer(msg.toByteArray());
	}
	
	public static ClientMessage buildClientMsg(String str, String hostname) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		ByteString reply = ByteString.copyFromUtf8(str);
		builder.setBody(reply);
		builder.setClientId("1");
		ClientMessage msg = builder.build();
		return msg;
	}
	
}
