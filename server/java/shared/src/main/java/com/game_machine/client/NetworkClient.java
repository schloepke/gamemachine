package com.game_machine.client;

public interface NetworkClient {
	void sendMessage(byte[] bytes);
	void start();
	void stop();
}
