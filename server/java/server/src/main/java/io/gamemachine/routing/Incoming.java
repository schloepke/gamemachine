package io.gamemachine.routing;

import io.gamemachine.authentication.Authentication;
import io.gamemachine.config.GameLimits;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.NetMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.ClientConnection;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.net.ClientInfo;
import io.gamemachine.net.Connection;
import io.gamemachine.net.udp.UdpServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Incoming extends UntypedActor {

	private static ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<String, ClientInfo>();

	public static String name = "incoming";
	private static Map<String, String> env = System.getenv();
	private ActorSelection gameHandler;
	private UdpServer udpServer;
	private Authentication authentication;
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private PlayerService playerService;

	public Incoming() {
		udpServer = UdpServer.getUdpServer();
		gameHandler = ActorUtil.getSelectionByName(RequestHandler.name);
		authentication = new Authentication();
		playerService = PlayerService.getInstance();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof NetMessage) {
			handleIncoming((NetMessage) message);
		}
	}

	private void handleIncoming(NetMessage netMessage) {
		String clientId = clientIdFromMessage(netMessage);
		ClientMessage clientMessage = netMessage.clientMessage;
		
		String gameId = playerService.getGameId(clientMessage.getPlayer().getId());
		GameLimits.incrementMessageCountIn(gameId);

		if (clientMessage.hasPlayerLogout()) {
			handleLogout(clientMessage, clientId);
		} else if (clientMessage.hasPlayerConnect()) {
			handleConnect(netMessage, clientMessage, clientId);
		} else {
			if (!clients.containsKey(clientMessage.getPlayer().getId())) {
				logger.debug("Ignoring message before connection setup");
				return;
			}
		}

		if (clientMessage.hasPlayer()) {
			if (!Authentication.isAuthenticated(clientMessage.getPlayer())) {
				logger.debug("Player not authenticated " + clientMessage.getPlayer().getId() + " authtoken="
						+ clientMessage.getPlayer().getAuthtoken());
				return;
			}
			gameHandler.tell(clientMessage, getSelf());
		}

	}

	private void handleConnect(NetMessage netMessage, ClientMessage clientMessage, String clientId) {
		logger.debug("PlayerConnect from " + clientMessage.getPlayer().getId());

		if (env.containsKey("CLUSTER_TEST")) {
			ensureTestUser(clientMessage.getPlayer());
		}

		if (!authentication.authenticate(clientMessage.getPlayer())) {
			logger.warning("Authentication failed for " + clientMessage.getPlayer().getId() + " authtoken="
					+ clientMessage.getPlayer().getAuthtoken());
			return;
		}

		String gameId = playerService.getGameId(clientMessage.getPlayer().getId());
		if (GameLimits.isConnectionLimitReached(gameId)) {
			logger.info("Connection limit reached for " + gameId);
			return;
		}

		// Destroy any possible existing client state and then create it. If
		// there is a connection then this can fail as the current
		// player actor will not die fast enough before the attempt to
		// create it fails because it's still there. Worst case scenario
		// we never get a playerdisconnect and the idle timeout cleans
		// things up. So eventually bad state is fixed.
		destroyChild(clientMessage.getPlayer().getId());
		clients.remove(clientMessage.getPlayer().getId());
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		ClientInfo clientInfo = new ClientInfo(netMessage.host, netMessage.port, netMessage.address, netMessage.ctx,
				clientConnection);
		clients.put(clientMessage.getPlayer().getId(), clientInfo);
		Connection connection = new Connection(netMessage.protocol, clientConnection, clientInfo, udpServer,
				clientMessage.getPlayer().getId());
		createChild(connection);
		RequestHandler.registerClient(clientMessage);
	}

	private void handleLogout(ClientMessage clientMessage, String clientId) {
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		logger.debug("PlayerLogout from " + clientMessage.getPlayer().getId());

		if (!Authentication.isAuthenticated(clientMessage.getPlayer())) {
			logger.debug("Unauthenticated client " + clientMessage.getPlayer().getId() + " attempting to logout");
			return;
		}

		if (hasClient(clientMessage.getPlayer().getId())) {
			destroyChild(clientMessage.getPlayer().getId());
			clients.remove(clientMessage.getPlayer().getId());
			RequestHandler.unregisterClient(clientMessage);
		}
	}

	public static boolean hasClient(String playerId) {
		return (clients.containsKey(playerId));
	}

	public static void removeClient(String playerId) {
		if (clients.containsKey(playerId)) {
			clients.remove(playerId);
		}
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
		clientConnection.setId(clientId).setGateway(Incoming.name).setServer("server");
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
		GameMachineLoader.getActorSystem().actorOf(Props.create(PlayerOutgoing.class, connection),
				connection.getPlayerId());
		String gameId = playerService.getGameId(connection.getPlayerId());
		GameLimits.incrementConnectionCount(gameId);
		logger.debug("Starting Outgoing actor " + connection.getPlayerId());
	}

	private void destroyChild(String playerId) {
		ActorSelection sel = ActorUtil.getSelectionByName(playerId);
		sel.tell(akka.actor.PoisonPill.getInstance(), getSelf());
		String gameId = playerService.getGameId(playerId);
		GameLimits.decrementConnectionCount(gameId);
		logger.debug("Stopping Outgoing actor " + playerId);
	}



}
