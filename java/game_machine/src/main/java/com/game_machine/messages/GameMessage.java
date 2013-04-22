package com.game_machine.messages;

public class GameMessage {

	public final byte[] bytes;
	public final String host;
	public final int port;
	
	public GameMessage(byte[] bytes, String host, int port) {
		this.bytes = bytes;
		this.host = host;
		this.port = port;
	}
}
