package com.game_machine.client.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.game_machine.client.NetworkClient;
import com.game_machine.client.agent.Config;
import com.game_machine.client.api.cloud.ObjectStore;


public class Api {

	
	private static ActorSystem actorSystem;
	private String playerId;
	private String authtoken;
	private NetworkClient networkClient;
	private Config conf = Config.getInstance();
	private ObjectStore cloud;
	
	public Api(String playerId, String authtoken, NetworkClient networkClient, ObjectStore cloud) {
		this.playerId = playerId;
		this.authtoken = authtoken;
		this.networkClient = networkClient;
		this.cloud = cloud;
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
	public ObjectStore getCloud() {
		return this.cloud;
	}
	
	/**
	 * @return A new {@link ApiMessage}
	 */
	public ApiMessage newMessage() {
		return new ApiMessage(playerId, authtoken, this);
	}

}
