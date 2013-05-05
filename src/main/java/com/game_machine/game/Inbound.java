package com.game_machine.game;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

import com.game_machine.NetMessage;
import com.game_machine.ProtobufMessages.ClientMessage;
import com.game_machine.net.client.MessageBuilder;

public class Inbound extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	private final Class<?> gameClass;
	
	public static Props mkProps(Class<?> gameClass, Integer numRoutes) {
		return Props.create(Inbound.class, gameClass).withRouter(new RoundRobinRouter(numRoutes));
	}
	
	public Inbound(Class<?> gameClass) {
		this.gameClass = gameClass;
		this.getContext().actorOf(Props.create(gameClass), gameClass.getSimpleName());
	}
	
	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage) message;
			if (netMessage.encoding == NetMessage.ENCODING_PROTOBUF) {
				ClientMessage clientMessage = MessageBuilder.decode(netMessage.bytes);
				netMessage = new NetMessage(clientMessage.getClientId(),netMessage.protocol, netMessage.encoding, clientMessage.getBody().toByteArray(), netMessage.host, netMessage.port);
			}
			this.getContext().child(gameClass.getSimpleName()).get().tell(netMessage, this.getSelf());
			log.debug("Inbound NetMessage message: {}", new String(netMessage.bytes));
		} else {
			unhandled(message);
		}
	}

}
