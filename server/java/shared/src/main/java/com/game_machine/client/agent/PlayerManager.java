package com.game_machine.client.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.game_machine.client.NetworkClient;
import com.game_machine.client.SimpleUdpClient;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.Cloud;

public class PlayerManager {

	private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
			
	private NetworkClient networkClient;
	private String host;
	private int port;
	private String playerId;
	private String gameId;
	private String authtoken;
	
	public PlayerManager(String host, int port, String gameId, String playerId, String authtoken) {
		this.host = host;
		this.port = port;
		this.gameId = gameId;
		this.playerId = playerId;
		this.authtoken = authtoken;
	}
	
	public void start() {
		Config conf = Runner.getConfig();
		String nodeHost = new Cloud().getNode();
		if (nodeHost != null) {
			host = nodeHost;
			logger.info("Node host="+host);
		} else {
			logger.warn("Unable to get node host");
		}
				
		networkClient = new SimpleUdpClient(host,port,playerId);
		networkClient.start();
		startPlayerActor();
		System.gc();
	}
	
	private void startPlayerActor() {
		ActorSystem system = Api.getActorSystem();
		Api api = new Api(playerId, authtoken, networkClient);
		system.actorOf(Props.create(PlayerActor.class, api, playerId), playerId);
	}
	
	private void stopPlayerActor() {
		ActorSelection sel = Api.getActorByName(playerId);
		sel.tell(akka.actor.PoisonPill.getInstance(), null);
	}
	
	public void stop() {
		stopPlayerActor();
		networkClient.stop();
	}
	
}
