package com.game_machine.net;

import GameMachine.Messages.ClientConnection;
import GameMachine.Messages.ClientMessage;

import com.game_machine.config.GameLimits;
import com.game_machine.core.PlayerService;
import com.game_machine.net.udp.UdpServer;
import com.game_machine.net.udp.UdpServerHandler;

public class Connection {

	private int protocol;
	private ClientConnection clientConnection;
	private ClientInfo clientInfo;
	private UdpServer udpServer;
	private String playerId;
	private String gameId;
	
	public Connection(int protocol,ClientConnection clientConnection,ClientInfo clientInfo,UdpServer server,String playerId) {
		this.setProtocol(protocol);
		this.setClientConnection(clientConnection);
		this.setClientInfo(clientInfo);
		this.setPlayerId(playerId);
		this.udpServer = server;
		this.gameId = PlayerService.getInstance().getGameId(playerId);
	}
  
	public void sendToClient(ClientMessage clientMessage) {
		if (protocol == 0) {
			byte[] bytes = clientMessage.toByteArray();
			GameLimits.addBytesTransferred(gameId, bytes.length);
			udpServer.sendToClient(clientInfo.address, bytes, clientInfo.ctx);
		} else if (protocol == 2) {
			clientMessage.setGameId(gameId);
			clientInfo.ctx.write(clientMessage);
			clientInfo.ctx.flush();
		} else {
			throw new RuntimeException("Invalid protocol "+protocol);
		}
		GameLimits.incrementMessageCountOut(gameId);
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
