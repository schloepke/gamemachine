package com.game_machine.net;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GameMachine.Messages.ChatDestroy;
import GameMachine.Messages.ClientManagerRegister;
import GameMachine.Messages.ClientManagerUnregister;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Player;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import com.game_machine.authentication.Authentication;
import com.game_machine.core.ActorUtil;
import com.game_machine.core.PlayerService;

public class RequestHandler extends UntypedActor {

	private Authentication authentication;
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private ActorSelection gameHandler;
	public static String name = "request_handler";

	public RequestHandler() {
		this.authentication = new Authentication();
		gameHandler = ActorUtil.getSelectionByName("game_handler");
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ClientMessage) {
			ClientMessage clientMessage = (ClientMessage) message;
			if (clientMessage.hasPlayerLogout()) {
				if (Authentication.isAuthenticated(clientMessage.getPlayer())) {
					unregisterClient(clientMessage);
				}
			} else if (clientMessage.hasPlayer()) {
				updateEntities(clientMessage);
				if (Authentication.isAuthenticated(clientMessage.getPlayer())) {
					gameHandler.tell(message, getSelf());
				} else {
					if (authentication.authenticate(clientMessage.getPlayer())) {
						if (!Incoming.clients.containsKey(clientMessage.getPlayer().getId())) {
							logger.info("No connection for "+clientMessage.getPlayer().getId());
							return;
						}
						registerClient(clientMessage);
						gameHandler.tell(message, getSelf());
					} else {
						logger.warn("Authentication failed for " + clientMessage.getPlayer().getId() + " authtoken="
								+ clientMessage.getPlayer().getAuthtoken());
					}
				}
			} else {
				unhandled(message);
			}
		} else {
			unhandled(message);
		}
	}

	private void updateEntities(ClientMessage clientMessage) {
		if (clientMessage.getEntityCount() >= 1) {
			for (Entity entity : clientMessage.getEntityList()) {
				entity.setPlayer(clientMessage.getPlayer());
			}
		}
	}

	public static void unregisterClient(ClientMessage clientMessage) {
		logger.debug("Register client " + clientMessage.getPlayer().getId());
		String playerId = clientMessage.getPlayer().getId();
		String clientId = clientMessage.getClientConnection().getId();
		Authentication.unregisterPlayer(playerId);
		ClientManagerUnregister unregister = new ClientManagerUnregister();
		unregister.setRegisterType("client");
		unregister.setName(clientId);
		Entity entity = new Entity();
		entity.setId(clientId);
		entity.setClientManagerUnregister(unregister);
		entity.setClientConnection(clientMessage.clientConnection);
		entity.setPlayer(clientMessage.getPlayer());
		ActorSelection sel = ActorUtil.getSelectionByName("GameMachine::ClientManager");
		sel.tell(entity, null);

		ChatDestroy chatDestroy = new ChatDestroy();
		chatDestroy.setPlayerId(playerId);
		sel = ActorUtil.getSelectionByName("GameMachine::GameSystems::ChatManager");
		sel.tell(chatDestroy, null);
	}

	private void registerClient(ClientMessage clientMessage) {
		logger.debug("Unregister client " + clientMessage.getPlayer().getId());
		String clientId = clientMessage.getClientConnection().getId();
		ClientManagerRegister register = new ClientManagerRegister();
		register.setRegisterType("client");
		register.setName(clientId);
		Entity entity = new Entity();
		entity.setId(clientId);
		entity.setClientManagerRegister(register);
		entity.setClientConnection(clientMessage.clientConnection);
		entity.setPlayer(clientMessage.getPlayer());
		ActorUtil.ask("GameMachine::ClientManager", entity, 50);
	}

}
