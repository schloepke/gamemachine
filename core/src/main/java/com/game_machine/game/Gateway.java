package com.game_machine.game;

import java.util.HashMap;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.ActorUtil;
import com.game_machine.GameMachine;
import com.game_machine.GatewayMessage;
import com.game_machine.NetMessage;
import com.game_machine.net.server.UdpServer;
import com.game_machine.net.server.UdtServer;

public class Gateway extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<String, NetMessage> clients = new HashMap<String, NetMessage>();

	public Gateway() {
	}

	public static void sendToClient(String clientId, byte[] bytes) {
		GatewayMessage gatewayMessage = new GatewayMessage(bytes,clientId);
		ActorUtil.getSelectionByClass(Gateway.class).tell(gatewayMessage, null);	
	}
	
	private String getClientId(NetMessage netMessage) {
		return netMessage.host+":"+netMessage.port;
	}

	public void onReceive(Object message) {
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage) message;
			GatewayMessage gatewayMessage = new GatewayMessage(netMessage.bytes,getClientId(netMessage));
			clients.put(gatewayMessage.getClientId(), netMessage);
			ActorUtil.getSelectionByClass(GameMachine.getGameHandler()).tell(gatewayMessage, this.getSelf());
			log.debug("Inbound NetMessage message: {}", new String(netMessage.bytes));

		} else if (message instanceof GatewayMessage) {
			GatewayMessage gatewayMessage = (GatewayMessage) message;
			log.debug("Outbound message: {}");
			NetMessage netMessage = clients.get(gatewayMessage.getClientId());

			if (netMessage.protocol == NetMessage.UDP) {
				UdpServer.getUdpServer().send(gatewayMessage.getBytes(), netMessage.host, netMessage.port, netMessage.ctx);
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
