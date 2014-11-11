package io.gamemachine.client;

public interface NetworkClient {
	void sendMessage(byte[] bytes);
	void start();
	void stop();
}
