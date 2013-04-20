package com.game_machine.server;

public interface GameProtocolServer {


	public void start();
	public void stop();
	public int getPort();
	
	public void sendMessage(String message, String host, int port);
}
