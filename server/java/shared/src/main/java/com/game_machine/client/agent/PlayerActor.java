package com.game_machine.client.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import Client.Messages.Agent;
import Client.Messages.ClientMessage;
import Client.Messages.Entity;
import Client.Messages.GameMessage;
import Client.Messages.Player;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.game_machine.client.api.Api;

public class PlayerActor extends UntypedActor {

	private String playerId;
	private boolean connected = false;
	private double lastUpdate = System.currentTimeMillis() + 10000;
	private Api api;
	private HashMap<String, ActorRef> runners = new HashMap<String, ActorRef>();
	private static double connectionTimeout = 10000;
	private static final Logger logger = LoggerFactory.getLogger(PlayerActor.class);

	public PlayerActor(Api api, String playerId) {
		this.playerId = playerId;
		this.api = api;
	}

	public void setDisconnected() {
		this.connected = false;
		logger.warn("Disconnected");
		ActorSelection sel = Api.getActorByName(AgentUpdateActor.class.getSimpleName());
		sel.tell("disconnected:" + playerId, getSelf());
	}

	public boolean isConnected() {
		return this.connected;
	}

	public void setConnected() {
		if (!isConnected()) {
			logger.info("Connected");
		}
		lastUpdate = System.currentTimeMillis();
		this.connected = true;

	}

	public void ping() {
		api.newMessage().setEchoTest().send();
	}

	public void connect() {
		lastUpdate = System.currentTimeMillis() + 10000;
		api.newMessage().setConnect().send();
		logger.info("PlayerConnect sent");
	}

	public void logout() {
		api.newMessage().setLogout().send();
	}

	public void handleEntities(ClientMessage clientMessage, Player player) {
		boolean hasGameMessage;
		for (Entity entity : clientMessage.getEntityList()) {
			hasGameMessage = false;
			if (entity.getGameMessages() != null) {
				for (GameMessage gameMessage : entity.getGameMessages().getGameMessageList()) {
					hasGameMessage = true;
					if (gameMessage.getAgentId() != null) {
						sendToAgent(gameMessage.getAgentId(), gameMessage);
					} else {
						sendToAgent("MessageRouter", gameMessage);
					}
				}
			}

			if (hasGameMessage) {
				continue;
			}

			if (entity.getEchoTest() != null) {
				setConnected();
				continue;
			}

			sendToAgent("MessageRouter", entity);
		}
	}

	private void sendToAgent(String name, Object message) {
		ActorRef runner = getRunner(name);
		if (runner != null) {
			runner.tell(message, getSelf());
		}
	}

	private List<String> agentIds(Agent agent) {
		List<String> agentIds = new ArrayList<String>();
		if (agent.getConcurrency() > 1) {
			for (int i = 0; i < agent.getConcurrency(); i++) {
				agentIds.add(agent.getId() + i);
			}
		} else {
			agentIds.add(agent.getId());
		}
		return agentIds;
	}

	private List<ActorRef> startRunners(Agent agent) {
		ActorRef runner;
		List<ActorRef> agentRunners = new ArrayList<ActorRef>();
		for (String agentId : agentIds(agent)) {
			runner = context().actorOf(Props.create(CodeblockRunner.class, api, agent, agentId), agentId);
			runners.put(agentId, runner);
			agentRunners.add(runner);
			logger.info("Starting agent " + agentId);
		}

		return agentRunners;
	}

	private void stopRunners(Agent agent) {
		ActorRef runner;
		for (String agentId : agentIds(agent)) {
			runner = getRunner(agentId);
			if (runner != null) {
				runner.tell(akka.actor.PoisonPill.getInstance(), getSelf());
				runners.remove(agentId);
				logger.info("Stopping agent " + agentId);
			}
		}

	}

	private List<ActorRef> getRunners(Agent agent) {
		List<ActorRef> agentRunners = new ArrayList<ActorRef>();
		for (String agentId : agentIds(agent)) {
			if (runners.containsKey(agentId)) {
				agentRunners.add(runners.get(agentId));
			}
		}
		return agentRunners;
	}

	private ActorRef getRunner(String id) {
		if (runners.containsKey(id)) {
			return runners.get(id);
		} else {
			return null;
		}
	}

	private void handleAgent(Agent agent) {
		List<ActorRef> agentRunners = getRunners(agent);

		if (agent.hasRemove()) {
			stopRunners(agent);
			return;
		}

		if (agentRunners.isEmpty()) {
			startRunners(agent);
		} else {
			for (ActorRef runner : agentRunners) {
				runner.tell(agent, getSelf());
			}
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Agent) {
			handleAgent((Agent) message);
			return;
		}

		if (message instanceof ClientMessage) {
			ClientMessage clientMessage = (ClientMessage) message;
			Player player = clientMessage.getPlayer();

			if (clientMessage.getPlayerConnected() != null) {
				logger.info("PlayerConnected received");
				setConnected();
			} else if (clientMessage.getPlayerLogout() != null) {
				context().stop(getSelf());
			}

			if (clientMessage.getEntityCount() >= 1) {
				handleEntities(clientMessage, player);
			}
		} else if (message instanceof String) {
			String imsg = (String) message;
			if (imsg.equals("check_connection")) {
				if ((System.currentTimeMillis() - lastUpdate) > connectionTimeout) {
					setDisconnected();
					logout();
					tick(3000, "connect");
					return;
				}

				if (isConnected()) {
					ping();
				}
				tick(3000, "check_connection");
			} else if (imsg.equals("connect")) {
				connect();
				tick(3000, "check_connection");
			}
		} else {
			unhandled(message);
		}
	}

	@Override
	public void preStart() {
		connect();
		tick(3000, "check_connection");
	}

	public void tick(int delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
