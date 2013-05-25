package com.game_machine.core.game;

import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMachine;
import com.game_machine.core.GatewayMessage;
import com.game_machine.core.NetMessage;
import com.game_machine.core.net.server.UdpServer;
import com.game_machine.core.net.server.UdtServer;
import com.game_machine.entity_system.Entities;
import com.game_machine.entity_system.generated.ClientConnection;
import com.game_machine.entity_system.generated.Components;
import com.game_machine.entity_system.generated.Entity;

public class Gateway extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<String, NetMessage> clients = new HashMap<String, NetMessage>();

	public Gateway() {
	}

	public static void send(String clientId, Entities entities) {
		byte[] bytes = Components.fromEntities(entities).toByteArray();
		send(clientId, bytes);
	}

	public static void send(String clientId, Entity entity) {
		Entities entities = new Entities();
		entities.addEntity(entity);
		send(clientId, entities);
	}

	public static void send(String clientId, byte[] bytes) {
		GatewayMessage gatewayMessage = new GatewayMessage(bytes, clientId);
		ActorUtil.getSelectionByClass(Gateway.class).tell(gatewayMessage, null);
	}

	private String getClientId(NetMessage netMessage) {
		return netMessage.host + ":" + netMessage.port;
	}

	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage) message;
			String clientId = getClientId(netMessage);

			GatewayMessage gatewayMessage = new GatewayMessage(netMessage.bytes, clientId);
			clients.put(clientId, netMessage);
			ActorUtil.getSelectionByClass(GameMachine.getGameHandler()).tell(gatewayMessage, this.getSelf());
			log.debug("Inbound NetMessage message: {}", new String(netMessage.bytes));

		} else if (message instanceof GatewayMessage) {
			GatewayMessage gatewayMessage = (GatewayMessage) message;
			log.debug("Outbound message: {}");
			NetMessage netMessage = clients.get(gatewayMessage.getClientId());

			if (netMessage.protocol == NetMessage.UDP) {
				UdpServer.getUdpServer().send(gatewayMessage.getBytes(), netMessage.host, netMessage.port,
						netMessage.ctx);
			} else if (netMessage.protocol == NetMessage.UDT) {
				UdtServer.getUdtServer().send(gatewayMessage.getBytes(), netMessage.ctx);
			} else {
				unhandled(message);
			}
		} else {
			unhandled(message);
		}
	}

}
