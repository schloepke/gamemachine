package io.gamemachine.client.agent;

import io.gamemachine.client.NetworkClient;
import io.gamemachine.client.SimpleUdpClient;
import io.gamemachine.client.api.Api;
import io.gamemachine.client.api.cloud.ObjectStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.google.common.util.concurrent.RateLimiter;

public class PlayerManager {

	private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
	private static final RateLimiter limiter = RateLimiter.create(Config.getInstance().getRateLimit());
	
	private NetworkClient networkClient;
	private String host;
	private int port;
	private String playerId;
	private String gameId;
	private int authtoken;
	private ObjectStore cloud;
	private Config conf;
	
	public PlayerManager(String host, int port, String gameId, String playerId, int authtoken) {
		this.host = host;
		this.port = port;
		this.gameId = gameId;
		this.playerId = playerId;
		this.authtoken = authtoken;
		conf = Runner.getConfig();
		this.cloud = new ObjectStore(conf.getCloudHost(),conf.getCloudUser(),conf.getCloudApiKey(),conf.getGameId(),limiter);
	}
	
	public boolean start() {
		String nodeHost = cloud.getNode();
		if (nodeHost != null) {
			host = nodeHost;
			logger.info("Node host="+host);
		} else {
			logger.error("Unable to get node host");
			return false;
		}
				
		networkClient = new SimpleUdpClient(host,port,playerId);
		networkClient.start();
		startPlayerActor();
		return true;
	}
	
	private void startPlayerActor() {
		ActorSystem system = Api.getActorSystem();
		Api api = new Api(playerId, authtoken, networkClient, this.cloud);
		system.actorOf(Props.create(PlayerActor.class, api, playerId), playerId);
	}
	
	private void stopPlayerActor() {
		ActorSelection sel = Api.getActorByName(playerId);
		sel.tell("shutdown",null);
		logger.info("Sleeping 5 seconds to allow for clean shutdown");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		sel.tell(akka.actor.PoisonPill.getInstance(), null);
	}
	
	public void stop() {
		stopPlayerActor();
		networkClient.stop();
	}
	
}
