package com.game_machine.game;

public final class EchoCommand {

	private final String clientId;
	
	public EchoCommand(String clientId) {
		this.clientId = clientId;
	}
	
	public String getClientId() {
		return this.clientId;
	}
}
