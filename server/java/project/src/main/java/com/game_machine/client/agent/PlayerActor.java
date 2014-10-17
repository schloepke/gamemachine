package com.game_machine.client.agent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.Agent;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import GameMachine.Messages.GameMessage;
import GameMachine.Messages.Player;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.game_machine.client.Api;
import com.game_machine.core.NetMessage;

public class PlayerActor extends UntypedActor {

	private String playerId;
	private boolean connected = false;
	private double lastUpdate = System.currentTimeMillis();
	private Api api;
	private HashMap<String, ActorRef> runners = new HashMap<String, ActorRef>();
	private static double connectionTimeout = 5000;
	private static final Logger logger = LoggerFactory.getLogger(PlayerActor.class);

	public PlayerActor(Api api, String playerId) {
		this.playerId = playerId;
		this.api = api;
	}

	public void setDisconnected() {
		if (isConnected()) {
			this.connected = false;
			logger.warn("Disconnected");
		}
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
		api.newMessage().setConnect().send();
	}

	public void logout() {
		api.newMessage().setLogout().send();
	}

	public void handleEntities(ClientMessage clientMessage, Player player) {
		for (Entity entity : clientMessage.getEntityList()) {
			if (entity.hasGameMessages()) {
				for (GameMessage gameMessage : entity.getGameMessages().getGameMessageList()) {
					if (gameMessage.hasAgentId()) {
						ActorRef runner = getRunner(gameMessage.getAgentId());
						if (runner != null) {
							runner.tell(gameMessage, getSelf());
						}
					}
				}
			}

			if (entity.hasEchoTest()) {
				setConnected();
			}
		}
	}

	private ActorRef startRunner(Agent agent) {
		ActorRef runner = context().actorOf(Props.create(CodeblockRunner.class, api, agent), agent.id);
		runners.put(agent.id, runner);
		logger.info("Starting agent " + agent.id);
		return runner;
	}

	private void stopRunner(String id) {
		ActorRef runner = getRunner(id);
		if (runner != null) {
			runner.tell(akka.actor.PoisonPill.getInstance(), getSelf());
			runners.remove(id);
			logger.info("Stopping agent " + id);

		}
	}

	private ActorRef getRunner(String id) {
		if (runners.containsKey(id)) {
			return runners.get(id);
		} else {
			return null;
		}
	}

	private void handleAgent(Agent agent) {
		ActorRef runner = getRunner(agent.id);

		if (runner != null && agent.hasRemove()) {
			stopRunner(agent.id);
			return;
		}

		if (runner == null) {
			runner = startRunner(agent);
		} else {
			runner.tell(agent, getSelf());
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Agent) {
			handleAgent((Agent) message);
			return;
		}

		if (message instanceof ClientMessage) {
			ClientMessage clientMessage = (ClientMessage)message;
			Player player = clientMessage.player;

			if (clientMessage.hasPlayerConnected()) {
				setConnected();
			} else if (clientMessage.hasPlayerLogout()) {
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
					tick(1000, "connect");
					return;
				}

				ping();
				tick(1000, "check_connection");
			} else if (imsg.equals("connect")) {
				connect();
				tick(1000, "check_connection");
			}
		} else {
			unhandled(message);
		}
	}

	@Override
	public void preStart() {
		connect();
		tick(1000, "check_connection");
	}

	public void tick(int delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
