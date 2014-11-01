package com.game_machine.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.ClientConnection;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Player;
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
import com.game_machine.core.PlayerService;
import com.game_machine.net.udp.UdpServer;

public class Incoming extends UntypedActor {

	public static ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<String, ClientInfo>();
	public static String name = "incoming";
	private static Map<String, String> env = System.getenv();
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
		if (netMessage.protocol == NetMessage.UDP) {
			clientMessage = ClientMessage.parseFrom(netMessage.bytes);
		} else if (netMessage.protocol == NetMessage.TCP) {
			clientMessage = netMessage.clientMessage;
		} else {
			throw new RuntimeException("Unknown protocol " + netMessage.protocol);
		}

		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		if (clientMessage.hasPlayerLogout()) {
			if (!authentication.authtokenIsValid(clientMessage.getPlayer())) {
				logger.debug("Unauthenticated client " + clientMessage.getPlayer().getId() + " attempting to logout");
				return;
			}
			destroyChild(clientMessage.getPlayer().getId());
			clients.remove(clientMessage.getPlayer().getId());

		} else if (clientMessage.hasPlayerConnect()) {
			if (env.containsKey("CLUSTER_TEST")) {
				ensureTestUser(clientMessage.getPlayer());
			}
			if (!authentication.authtokenIsValid(clientMessage.getPlayer())) {
				logger.debug("Unauthenticated client " + clientMessage.getPlayer().getId() + " attempting to login");
				return;
			}

			destroyChild(clientMessage.getPlayer().getId());
			clients.remove(clientMessage.getPlayer().getId());

			if (!clients.containsKey(clientMessage.getPlayer().getId())) {
				ClientInfo clientInfo = new ClientInfo(netMessage.host, netMessage.port, netMessage.address,
						netMessage.ctx, clientConnection);
				clients.put(clientMessage.getPlayer().getId(), clientInfo);
				Connection connection = new Connection(netMessage.protocol, clientConnection, clientInfo, udpServer,
						clientMessage.getPlayer().getId());
				createChild(connection);
			}
		} else {
			if (!clients.containsKey(clientMessage.getPlayer().getId())) {
				logger.debug("Ignoring message before connection setup");
				return;
			}
		}
		gameHandler.tell(clientMessage, getSelf());
	}

	private void ensureTestUser(Player player) {
		PlayerService playerService = PlayerService.getInstance();
		Player p = playerService.find(player.getId());
		if (p == null) {
			playerService.create(player.getId(), "cluster_test");
		}
		playerService.setAuthtoken(player.getId(), player.getAuthtoken());
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
		GameMachineLoader.getActorSystem().actorOf(
				Props.create(Outgoing.class, connection), connection.getPlayerId());
		logger.debug("Starting Outgoing actor " + connection.getPlayerId());
	}

	private void destroyChild(String playerId) {
		ActorSelection sel = ActorUtil.getSelectionByName(playerId);
		sel.tell(akka.actor.PoisonPill.getInstance(), getSelf());
		logger.debug("Stoppping Outgoing actor " + playerId);
	}

}
