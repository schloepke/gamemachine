package com.game_machine.core.actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

import com.game_machine.game.actors.Game;
import com.game_machine.messages.NetMessage;
import com.game_machine.messages.ProtobufMessages.ClientMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public class Inbound extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public Inbound() {
		this.getContext().actorOf(Props.create(Game.class), Game.class.getSimpleName());
	}
	
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage) message;
			if (netMessage.encoding == NetMessage.ENCODING_PROTOBUF) {
				netMessage = NetMessage.copy(netMessage,decode(netMessage.bytes).getBody().toByteArray());
			}
			this.getContext().child(Game.class.getSimpleName()).get().tell(netMessage, this.getSelf());
			log.info("Inbound NetMessage message: {}", message);
		} else {
			unhandled(message);
		}
	}

	private ClientMessage decode(byte[] bytes) {
		try {
			return ClientMessage.parseFrom(bytes);
		} catch (InvalidProtocolBufferException e1) {
			log.warning("BYTES: " + bytes.length + " " + new String(bytes));
			e1.printStackTrace();
			throw new RuntimeException("Decoding Error");
		}
	}
}
