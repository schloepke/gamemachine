package io.gamemachine.net;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.config.GameLimits;
import io.gamemachine.core.NetMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.ClientConnection;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.net.tcp.TcpServerHandler;
import io.gamemachine.net.udp.NettyUdpServerHandler;
import io.gamemachine.net.udp.SimpleUdpServer;

public class Connection {

	private static ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
	private static final Logger logger = LoggerFactory.getLogger(Connection.class);
	
	public int protocol;
	private ClientConnection clientConnection;
	private String playerId;
	private String gameId;
	public long clientId;
	public int ip;
	private boolean playerIsAgent = false;

	public Connection(int protocol, int ip, ClientConnection clientConnection, String playerId, long clientId) {
		this.protocol = protocol;
		this.ip = ip;
		this.clientConnection = clientConnection;
		this.playerId = playerId;
		this.clientId = clientId;
		PlayerService playerService = PlayerService.getInstance();
		this.gameId = playerService.getGameId(playerId);
		this.playerIsAgent = playerService.playerIsAgent(playerId);
		playerService.setIp(this.playerId, this.ip);
	}

	public static Connection getConnection(String playerId) {
		return connections.get(playerId);
	}
	
	public static Set<String> getConnectedPlayerIds() {
		return connections.keySet();
	}
	
	public static boolean hasConnection(String playerId) {
		return (connections.containsKey(playerId));
	}
	
	public static void addConnection(String playerId, Connection connection) {
		connections.put(playerId, connection);
	}

	public static void removeConnection(String playerId) {
		if (connections.containsKey(playerId)) {
			Connection connection = connections.get(playerId);
			if (connection != null) {
				if (connection.protocol == NetMessage.SIMPLE_UDP) {
					SimpleUdpServer.removeClient(connection.clientId);
				} else if (connection.protocol == NetMessage.NETTY_UDP) {
					NettyUdpServerHandler.removeClient(connection.clientId);
				} else if (connection.protocol == NetMessage.TCP) {
					TcpServerHandler.removeClient(connection.clientId);
				}
			}
			
			connections.remove(playerId);
		}
	}
	
	public void sendToClient(ClientMessage clientMessage) {
		if (protocol == NetMessage.NETTY_UDP || protocol == NetMessage.SIMPLE_UDP) {
			byte[] bytes = clientMessage.toByteArray();

			// Don't count data transfers on local network
			if (!playerIsAgent) {
				GameLimits.addBytesTransferred(gameId, playerId,bytes.length);
			}

			if (protocol == NetMessage.SIMPLE_UDP) {
				SimpleUdpServer.sendMessage(clientId, bytes);
			} else {
				NettyUdpServerHandler.sendMessage(clientId, bytes);
			}
		} else if (protocol == NetMessage.TCP) {

			// Have to pass the game id through here so tcp encoder can call
			// addBytesTransferred
			if (!playerIsAgent) {
				clientMessage.setGameId(gameId);
			}

			TcpServerHandler.sendMessage(clientId, clientMessage);
		} else {
			throw new RuntimeException("Invalid protocol " + protocol);
		}
		GameLimits.incrementMessageCountOut(gameId);
	}

	public long getClientId() {
		return clientId;
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public ClientConnection getClientConnection() {
		return clientConnection;
	}

}
