package com.game_machine.systems;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.messages.GameMessage;
import com.game_machine.messages.ProtobufMessages.ClientMsg;
import com.google.protobuf.InvalidProtocolBufferException;

public class Inbound extends UntypedActor {

	public Inbound() {
		this.getContext().actorOf(new Props(Game.class), "game");
	}

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void onReceive(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage) message;
			this.getContext().child("game").get().tell(gameMessage, this.getSelf());
			log.info("Inbound GameMessage message: {}", message);
		} else {
			unhandled(message);
		}
	}

	private void decode(GameMessage gameMessage) {
		ClientMsg msg = null;
		try {
			msg = ClientMsg.parseFrom(gameMessage.bytes);
			ClientMsg.Builder builder = msg.toBuilder();
			builder.setHostname(gameMessage.host);
			builder.setPort(gameMessage.port);
			msg = builder.build();
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
		}
	}
}
