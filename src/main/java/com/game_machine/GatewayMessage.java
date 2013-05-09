package com.game_machine;

public final class GatewayMessage {

	private final byte[] bytes;
	private final String clientId;
	
	public GatewayMessage(byte[] bytes, String clientId) {
		this.bytes = bytes;
		this.clientId = clientId;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
	public String getClientId() {
		return this.clientId;
	}
}
