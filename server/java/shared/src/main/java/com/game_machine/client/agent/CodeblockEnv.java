package com.game_machine.client.agent;

import akka.actor.ActorSelection;

import com.game_machine.client.api.Api;

public class CodeblockEnv {

	private Api api;
	private CodeblockRunner runner;
	private int reloadCount = 0;
	private String agentId;
	private String playerId;
	
	public CodeblockEnv(Api api, CodeblockRunner runner, String agentId) {
		this.api = api;
		this.runner = runner;
		this.agentId = agentId;
		this.playerId = api.getPlayerId();
	}

	public String getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * @return the agent id
	 */
	public String getAgentId() {
		return this.agentId;
	}
	
	public Api getApi() {
		return api;
	}

	public void sendToAgent(String agentId, Object message) {
		ActorSelection sel = Api.getActorByName(playerId+"/"+agentId);
		sel.tell(message, runner.getSelf());
	}
	
	public String getSender() {
		return runner.getSender().path().name();
	}
	
	public void tick(int delay, Object message) {
		runner.tick(delay, message);
	}
	
	public int getReloadCount() {
		return reloadCount;
	}

	public void incReloadCount() {
		this.reloadCount++;
	}
	
	public void resetReloadCount() {
		this.reloadCount = 0;
	}
}
