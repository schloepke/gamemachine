package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MessageGateway extends UntypedActor {

	public static ConcurrentHashMap<String, NetMessage> netMessages = new ConcurrentHashMap<String, NetMessage>();
	public static String name = "message_gateway";
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection udpIncoming;
	private ActorSelection entityTracking;
	private UdpServer udpServer;
	
	public MessageGateway()
	{
		udpServer = UdpServer.getUdpServer();
		udpIncoming = ActorUtil.getSelectionByName("GameMachine::Endpoints::UdpIncoming");
		entityTracking = ActorUtil.getSelectionByName("fastpath_entity_tracking");
		Commands.clientManagerRegister(name);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		// Incoming message
		if (message instanceof NetMessage) {
			NetMessage netMessage = (NetMessage)message;
			if (netMessage.clientMessage.fastpath != null) {
				ClientMessage clientMessage = netMessage.clientMessage;
				
				if (!netMessages.containsKey(clientMessage.player.id)) {
					netMessages.put(clientMessage.player.id, netMessage);
				}
				routeFastpath(clientMessage);
			} else {
				udpIncoming.tell(message, null);
			}
			
		// outgoing message
		} else if (message instanceof Entity) {
			Entity entity = (Entity)message;
			NetMessage netMessage = netMessages.get(entity.player.id);
			ClientMessage clientMessage = new ClientMessage();
			//clientMessage.setClientConnection(netMessage.clientMessage.clientConnection);
			clientMessage.addEntity(entity);
			byte[] bytes = clientMessage.toByteArray();
			udpServer.sendToClient(bytes, netMessage.host, netMessage.port, netMessage.ctx);
			
		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent)message;
			if (event.event.equals("disconnected")) {
				netMessages.remove(event.player_id);
			}
		}
		
	}
	
	private void routeFastpath(ClientMessage clientMessage) {
		for(Entity entity : clientMessage.getEntityList()) {
			entity.setPlayer(clientMessage.player);
			if (entity.hasTrackEntity() || entity.hasGetNeighbors()) {
				entityTracking.tell(entity, getSelf());
			} else {
				log.warning("Unable to find destination for entity id "+entity.id);
			}
		}
	}

}
