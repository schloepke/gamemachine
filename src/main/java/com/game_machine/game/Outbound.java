package com.game_machine.game;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.NetMessage;
import com.game_machine.net.client.MessageBuilder;
import com.game_machine.net.server.UdpServer;
import com.game_machine.net.server.UdtServer;

public class Outbound extends UntypedActor {

	public Outbound() {
	}

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);


	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			log.debug("Outbound message: {}");
			NetMessage netMessage = (NetMessage) message;
			byte[] bytesToSend = netMessage.bytes;
			
			if (netMessage.encoding == NetMessage.ENCODING_PROTOBUF) {
				bytesToSend = MessageBuilder.encode(netMessage.bytes,netMessage.clientId).toByteArray();
			}
			
			if (netMessage.protocol == NetMessage.UDP) {
				UdpServer.getUdpServer().send(bytesToSend, netMessage.host, netMessage.port);
			} else if (netMessage.protocol == NetMessage.UDT) {
				UdtServer.getUdtServer().send(bytesToSend, netMessage.host, netMessage.port);
			} else {
				unhandled(message);
			}
			
		} else {
			unhandled(message);
		}
	}
}
