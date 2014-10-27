package com.game_machine.net;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.ClientConnection;
import GameMachine.Messages.ClientMessage;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.game_machine.authentication.Authentication;
import com.game_machine.authentication.AuthorizedPlayers;
import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMachineLoader;
import com.game_machine.core.NetMessage;
import com.game_machine.core.UdpServer;

public class Incoming extends UntypedActor {

	private static ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<String, ClientInfo>();
	public static String name = "incoming";
	private String gameHandlerName = "request_handler";
	private ActorSelection gameHandler;
	private UdpServer udpServer;
	private Authentication authentication;
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public Incoming() {
		udpServer = UdpServer.getUdpServer();
		gameHandler = ActorUtil.getSelectionByName(gameHandlerName);
		authentication = new Authentication();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			handleIncoming((NetMessage) message);
		}
	}

	private void handleIncoming(NetMessage netMessage) {
		String clientId = clientIdFromMessage(netMessage);
		ClientMessage clientMessage = null;
		if (netMessage.protocol == 0) {
			clientMessage = ClientMessage.parseFrom(netMessage.bytes);
		} else if (netMessage.protocol == 2) {
			clientMessage = netMessage.clientMessage;
		} else {
			throw new RuntimeException("Unknown protocol " + netMessage.protocol);
		}

		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		if (clientMessage.hasPlayerConnect() || clientMessage.hasPlayerLogout()) {
			if (!authentication.authtokenIsValid(clientMessage.getPlayer())) {
				if (clientMessage.hasPlayerLogout()) {
					logger.warning("Unauthenticated client " + clientMessage.getPlayer().getId()
							+ " attempting to logout");
				} else if (clientMessage.hasPlayerConnect()) {
					logger.warning("Unauthenticated client " + clientMessage.getPlayer().getId()
							+ " attempting to login");
				}
				return;
			}

			destroyChild(clientMessage.getPlayer().getId());
			clients.remove(clientMessage.getPlayer().getId());

			if (clientMessage.hasPlayerConnect()) {
				if (!clients.containsKey(clientMessage.getPlayer().getId())) {
					ClientInfo clientInfo = new ClientInfo(netMessage.host, netMessage.port, netMessage.address,
							netMessage.ctx, clientConnection);
					clients.put(clientMessage.getPlayer().getId(), clientInfo);
					Connection connection = new Connection(netMessage.protocol, clientConnection, clientInfo,
							udpServer, clientMessage.getPlayer().getId());
					createChild(connection);
				}
			}
		}

		gameHandler.tell(clientMessage, getSelf());
	}

	private String clientIdFromMessage(NetMessage netMessage) {
		return netMessage.host + ":" + netMessage.port;
	}

	private ClientConnection createClientConnection(String clientId, ClientMessage clientMessage) {
		ClientConnection clientConnection = new ClientConnection();
		clientConnection.setId(clientId).setGateway("Incoming").setServer("server");
		clientConnection.setType(clientConnectionType(clientMessage));
		return clientConnection;
	}

	private String clientConnectionType(ClientMessage clientMessage) {
		if (clientMessage.hasConnection_type()) {
			if (clientMessage.getConnection_type() == 1) {
				return "region";
			} else if (clientMessage.getConnection_type() == 2) {
				return "cluster";
			} else {
				return "combined";
			}
		} else {
			return "combined";
		}
	}

	private void createChild(Connection connection) {
		GameMachineLoader.getActorSystem().actorOf(Props.create(Outgoing.class, connection), connection.getPlayerId());
		logger.info("Starting Outgoing actor " + connection.getPlayerId());
	}

	private void destroyChild(String playerId) {
		ActorSelection sel = ActorUtil.getSelectionByName(playerId);
		sel.tell(akka.actor.PoisonPill.getInstance(), getSelf());
		logger.info("Stoppping Outgoing actor " + playerId);
	}

}
