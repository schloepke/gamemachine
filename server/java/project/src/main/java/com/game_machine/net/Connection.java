package com.game_machine.net;

import GameMachine.Messages.ClientConnection;
import GameMachine.Messages.ClientMessage;

import com.game_machine.core.UdpServer;
import com.game_machine.core.UdpServerHandler;

public class Connection {

	private int protocol;
	private ClientConnection clientConnection;
	private ClientInfo clientInfo;
	private UdpServer udpServer;
	private String playerId;
	
	public Connection(int protocol,ClientConnection clientConnection,ClientInfo clientInfo,UdpServer server,String playerId) {
		this.setProtocol(protocol);
		this.setClientConnection(clientConnection);
		this.setClientInfo(clientInfo);
		this.setPlayerId(playerId);
		this.udpServer = server;
	}
  
	public void sendToClient(ClientMessage clientMessage) {
		if (protocol == 0) {
			byte[] bytes = clientMessage.toByteArray();
			udpServer.sendToClient(clientInfo.address, bytes, clientInfo.ctx);
		} else if (protocol == 2) {
			clientInfo.ctx.write(clientMessage);
			clientInfo.ctx.flush();
			UdpServerHandler.countOut.incrementAndGet();
		} else {
			throw new RuntimeException("Invalid protocol "+protocol);
		}
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public UdpServer getUdpServer() {
		return udpServer;
	}

	public void setUdpServer(UdpServer udpServer) {
		this.udpServer = udpServer;
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	public ClientConnection getClientConnection() {
		return clientConnection;
	}

	public void setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
}
