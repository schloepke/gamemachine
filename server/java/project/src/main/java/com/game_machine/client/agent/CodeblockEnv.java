package com.game_machine.client.agent;

import com.game_machine.client.Api;

public class CodeblockEnv {

	private Api api;
	private CodeblockRunner runner;
	private int reloadCount = 0;
	
	public CodeblockEnv(Api api, CodeblockRunner runner) {
		this.api = api;
		this.runner = runner;
	}

	public Api getApi() {
		return api;
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
