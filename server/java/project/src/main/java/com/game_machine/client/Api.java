package com.game_machine.client;

import com.game_machine.client.agent.CodeblockRunner;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;


public class Api {

	private MessageApi messageApi;
	private static ActorSystem actorSystem;
	private String playerId;
	
	public Api(String playerId, String authtoken, NetworkClient networkClient) {
		this.playerId = playerId;
		this.messageApi = new MessageApi(playerId, authtoken, networkClient);
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
	
	public MessageApi messaging() {
		return this.messageApi;
	}

}
