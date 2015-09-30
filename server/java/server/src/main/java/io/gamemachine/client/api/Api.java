package io.gamemachine.client.api;

import io.gamemachine.client.NetworkClient;
import io.gamemachine.client.agent.Config;
import io.gamemachine.client.api.cloud.ObjectStore;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;


public class Api {

	
	private static ActorSystem actorSystem;
	private String playerId;
	private int authtoken;
	private NetworkClient networkClient;
	private Config conf = Config.getInstance();
	private ObjectStore objectStore;
	
	public Api(String playerId, int authtoken, NetworkClient networkClient, ObjectStore objectStore) {
		this.playerId = playerId;
		this.authtoken = authtoken;
		this.networkClient = networkClient;
		this.objectStore = objectStore;
	}
	
	public static class RateLimitExceeded extends RuntimeException {
		private static final long serialVersionUID = 8259379701319382628L;

		public RateLimitExceeded(String message){
		      super(message);
		   }
	}
	
	public static void setActorSystem(ActorSystem actorSystem) {
		Api.actorSystem = actorSystem;
	}
	
	public static ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	public static ActorSelection getActorByClass(Class<?> klass) {
		return Api.actorSystem.actorSelection("/user/" + klass.getSimpleName());
	}

	public static ActorSelection getActorByName(String name) {
		return Api.actorSystem.actorSelection("/user/" + name);
	}
	
	
	/**
	 * @return The player id of the agent controller
	 */
	public String getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * @return The game id of the agent controller
	 */
	public String getGameid() {
		return conf.getGameId();
	}
	
	/**
	 * @return The api key for this account
	 */
	public String getApiKey() {
		return conf.getCloudApiKey();
	}
	
	/**
	 * @return  The cloud api server hostname
	 */
	public String getCloudHost() {
		return conf.getCloudHost();
	}
	
	public void sendToNetwork(byte[] bytes) {
		networkClient.sendMessage(bytes);
	}
	
	/**
	 * @return The {@link ObjectStore} instance
	 */
	public ObjectStore getObjectStore() {
		return this.objectStore;
	}
	
	/**
	 * @return A new {@link ApiMessage}
	 */
	public ApiMessage newMessage() {
		return new ApiMessage(playerId, authtoken, this);
	}

}
