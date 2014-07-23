package com.game_machine.core;

import GameMachine.Messages.GameMessage;
import GameMachine.Messages.MonoMessage;

public class MonoProxy {

	private UdpClient udpClient;
	
	public MonoProxy(int port, int timeout) {
		udpClient = new UdpClient(port,timeout);
		if (!udpClient.connect()) {
			throw new RuntimeException("Unable to connect to mono");
		}
	}
	
	public GameMessage call(String methodName, GameMessage gameMessage) {
		MonoMessage message = new MonoMessage();
		message.setMethodName(methodName);
		message.setGameMessage(gameMessage);
		byte[] bytes = call(message.toByteArray());
		if (bytes == null) {
			return null;
		} else {
			message = MonoMessage.parseFrom(bytes);
			return message.gameMessage;
		}
	}
	
	public byte[] call(byte[] bytes) {
		return udpClient.send(bytes);
	}
	
}
