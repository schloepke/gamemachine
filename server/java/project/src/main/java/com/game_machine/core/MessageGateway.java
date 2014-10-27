package com.game_machine.core;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Player;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MessageGateway extends UntypedActor {

	public static AtomicInteger messageCount;
	public static ConcurrentHashMap<String, NetMessage> netMessages = new ConcurrentHashMap<String, NetMessage>();
	public static String name = "message_gateway";

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorSelection incoming;
	private ActorSelection entityTracking;
	private UdpServer udpServer;
	private Boolean fastpathOnly;

	public MessageGateway() {
		udpServer = UdpServer.getUdpServer();
		incoming = ActorUtil.getSelectionByName("incoming");
		entityTracking = ActorUtil.getSelectionByName("fastpath_entity_tracking");
		Commands.clientManagerRegister(name);
		messageCount = new AtomicInteger();
	}

	@Override
	public void onReceive(Object message) throws Exception {

		// Incoming message
		if (message instanceof NetMessage) {
			messageCount.incrementAndGet();
			NetMessage netMessage = (NetMessage) message;
			ClientMessage clientMessage;

			if (netMessage.protocol == NetMessage.UDP) {
				clientMessage = ClientMessage.parseFrom(netMessage.bytes);
			} else if (netMessage.protocol == NetMessage.TCP) {
				clientMessage = netMessage.clientMessage;
			} else {
				clientMessage = null;
			}

			if (!netMessages.containsKey(clientMessage.player.id)) {
				netMessages.put(clientMessage.player.id, netMessage);
			}

			List<Entity> entities = clientMessage.getEntityList();

			if (entities == null) {
				incoming.tell(message, null);
			} else {
				fastpathOnly = true;
				for (Entity entity : clientMessage.getEntityList()) {
					if (entity.hasFastpath()) {
						routeFastpath(clientMessage.player, entity);
					} else {
						fastpathOnly = false;
					}
				}

				if (!fastpathOnly) {
					incoming.tell(message, null);
				}
			}

			// outgoing message
		} else if (message instanceof Entity) {
			Entity entity = (Entity) message;
			NetMessage netMessage = netMessages.get(entity.player.id);
			ClientMessage clientMessage = new ClientMessage();
			clientMessage.addEntity(entity);

			if (netMessage.protocol == NetMessage.UDP) {
				byte[] bytes = clientMessage.toByteArray();
				udpServer.sendToClient(netMessage.address, bytes, netMessage.ctx);
			} else if (netMessage.protocol == NetMessage.TCP) {
				netMessage.ctx.write(clientMessage);
				UdpServerHandler.countOut.getAndIncrement();
			}

		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent) message;
			log.debug("Message gateway got client manager event " + event.event);
			if (event.event.equals("disconnected")) {
				netMessages.remove(event.player_id);
				log.debug("Player " + event.player_id + " removed from message gateway");
			}
		}

	}

	private void routeFastpath(Player player, Entity entity) {
		entity.setPlayer(player);
		if (entity.hasTrackData()) {
			entityTracking.tell(entity, getSelf());
		} else {
			log.warning("Unable to find destination for entity id " + entity.id);
		}
	}

}
