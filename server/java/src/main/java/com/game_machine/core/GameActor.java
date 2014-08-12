package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.DeliveryConfirmation;
import GameMachine.Messages.Entity;
import akka.actor.UntypedActor;

public class GameActor extends UntypedActor {

	private static ConcurrentHashMap<String, Boolean> reliableMessages = new ConcurrentHashMap<String, Boolean>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		
	}
	
	public static void removeReliableMessage(String id) {
		if (reliableMessages.containsKey(id)) {
			reliableMessages.remove(id);
		}
	}
	
	public boolean confirmDelivery(Entity entity) {
		if (reliableMessages.containsKey(entity.id)) {
			reliableMessages.replace(entity.id, true);
			sendDeliveryConfirmation(entity.id,entity.getPlayer().id);
			return true;
		} else {
			return false;
		}
	}
	
	public void sendDeliveryConfirmation(String entityId, String playerId) {
		Entity entity = new Entity();
		DeliveryConfirmation deliveryConfirmation = new DeliveryConfirmation();
		deliveryConfirmation.messageId = entityId;
		entity.setDeliveryConfirmation(deliveryConfirmation);
		PlayerCommands.sendToPlayer(entity, playerId);
	}
	
	public boolean exactlyOnce(Object message) {
		if (message instanceof Entity) {
			Entity entity = (Entity)message;
			if (entity.hasReliable()) {
				if (reliableMessages.containsKey(entity.id)) {
					Boolean confirmed = reliableMessages.get(entity.id);
					if (confirmed) {
						sendDeliveryConfirmation(entity.id,entity.getPlayer().id);
					}
				} else {
					reliableMessages.put(entity.id, false);
					return true;
				}
			}
		}
		return false;
	}
}
