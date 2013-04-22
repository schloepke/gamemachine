package com.game_machine.systems;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.NetMessage;
import com.game_machine.messages.ProtobufMessages.ClientMessage;
import com.game_machine.server.Server;
import com.google.protobuf.ByteString;

public class Outbound extends UntypedActor {

	public Outbound() {
	}

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);


	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			log.info("Outbound GameMessage message: {}", message);
			NetMessage netMessage = (NetMessage) message;
			byte[] bytesToSend = netMessage.bytes;
			
			if (netMessage.encoding == NetMessage.ENCODING_PROTOBUF) {
				bytesToSend = encode(netMessage.bytes);
			}
			
			if (netMessage.protocol == NetMessage.UDP) {
				Server.udpServer.send(bytesToSend, netMessage.host, netMessage.port);
			} else if (netMessage.protocol == NetMessage.UDT) {
				unhandled(message);
			} else {
				unhandled(message);
			}
			
		} else {
			unhandled(message);
		}
	}
	
	public byte[] encode(byte[] bytes) {
		ClientMessage.Builder builder = ClientMessage.newBuilder();
		ByteString byteString = ByteString.copyFrom(bytes);
		builder.setBody(byteString);
		ClientMessage msg = builder.build();
		return msg.toByteArray();
	}
}
