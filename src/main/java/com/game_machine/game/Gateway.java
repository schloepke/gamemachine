package com.game_machine.game;

import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.ActorUtil;
import com.game_machine.GameMachine;
import com.game_machine.MessageBuilder;
import com.game_machine.NetMessage;
import com.game_machine.Pb.ClientMessage;
import com.game_machine.net.server.UdpServer;
import com.game_machine.net.server.UdtServer;

public class Gateway extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<String, NetMessage> clients = new HashMap<String, NetMessage>();
	
	public Gateway() {
	}

	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage) message;
			if (netMessage.encoding == NetMessage.ENCODING_PROTOBUF) {
				ClientMessage clientMessage = MessageBuilder.decode(netMessage.bytes);
				clients.put(clientMessage.getPlayers(0).getName(), netMessage);
				ActorUtil.getSelectionByClass(GameMachine.getGameHandler()).tell(clientMessage, this.getSelf());
				log.debug("Inbound NetMessage message: {}", new String(netMessage.bytes));
			} else {
				unhandled(message);
			}
		} else if (message instanceof ClientMessage) {
			ClientMessage clientMessage = (ClientMessage) message;
			NetMessage netMessage = clients.get(clientMessage.getPlayers(0).getName());
			
			if (netMessage.protocol == NetMessage.UDP) {
				UdpServer.getUdpServer().send(clientMessage.toByteArray(), netMessage.host, netMessage.port, netMessage.ctx);
			} else if (netMessage.protocol == NetMessage.UDT) {
				UdtServer.getUdtServer().send(clientMessage.toByteArray(), netMessage.ctx);
			} else {
				unhandled(message);
			}
		} else {
			unhandled(message);
		}
	}

}
