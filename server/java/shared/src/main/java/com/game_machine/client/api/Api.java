package com.game_machine.client.api;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.game_machine.client.NetworkClient;
import com.game_machine.client.agent.Config;


public class Api {

	private static ActorSystem actorSystem;
	private String playerId;
	private String authtoken;
	private NetworkClient networkClient;
	private Config conf = Config.getInstance();
	private Cloud cloud;
	
	public Api(String playerId, String authtoken, NetworkClient networkClient) {
		this.playerId = playerId;
		this.authtoken = authtoken;
		this.networkClient = networkClient;
		this.cloud = new Cloud();
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
	
	public String getPlayerId() {
		return this.playerId;
	}
	
	public String getGameid() {
		return conf.getGameId();
	}
	
	public String getApiKey() {
		return conf.getCloudApiKey();
	}
	
	public String getCloudHost() {
		return conf.getCloudHost();
	}
	
	public void send(byte[] bytes) {
		networkClient.sendMessage(bytes);
	}
	
	public Cloud getCloud() {
		return this.cloud;
	}
	
	public ApiMessage newMessage() {
		return new ApiMessage(playerId, authtoken, this);
	}

}
