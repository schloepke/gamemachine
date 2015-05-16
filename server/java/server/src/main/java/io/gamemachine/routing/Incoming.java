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
import io.gamemachine.net.Connection;

import java.util.Map;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Incoming extends UntypedActor {

	public static String name = "incoming";
	private static Map<String, String> env = System.getenv();
	private ActorSelection gameHandler;
	private Authentication authentication;
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private PlayerService playerService;

	public Incoming() {
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
		long clientId = netMessage.clientId;
		ClientMessage clientMessage = netMessage.clientMessage;

		if (clientMessage.hasPlayerLogout()) {
			handleLogout(clientMessage, clientId);
		} else if (clientMessage.hasPlayerConnect()) {
			handleConnect(netMessage, clientMessage, clientId);
		} else {
			if (!Connection.hasConnection(clientMessage.player.id)) {
				logger.debug("Ignoring message before connection setup");
				return;
			}
		}

		if (clientMessage.hasPlayer()) {
			String gameId = playerService.getGameId(clientMessage.player.id);
			GameLimits.incrementMessageCountIn(gameId);
			
			if (!Authentication.isAuthenticated(clientMessage.getPlayer())) {
				logger.debug("Player not authenticated " + clientMessage.getPlayer().getId() + " authtoken="
						+ clientMessage.getPlayer().getAuthtoken());
				return;
			}
			gameHandler.tell(clientMessage, getSelf());
		}

	}

	private void handleConnect(NetMessage netMessage, ClientMessage clientMessage, long clientId) {
		logger.debug("PlayerConnect from " + clientMessage.getPlayer().getId());

		if (env.containsKey("CLUSTER_TEST")) {
			ensureTestUser(clientMessage.getPlayer());
		}

		if (!authentication.authenticate(clientMessage.getPlayer())) {
			logger.warning("Authentication failed for " + clientMessage.player.id + " authtoken="
					+ clientMessage.getPlayer().getAuthtoken());
			return;
		}

		String gameId = playerService.getGameId(clientMessage.player.id);
		if (GameLimits.isConnectionLimitReached(gameId)) {
			logger.info("Connection limit reached for " + gameId);
			return;
		}

		// We can't assume that a client logged out correctly, we might still have a connection for them.  Just destroy it if it exists
		// and create a new one.  Under load it's possible that this can fail.  The existing child actor might not get the kill request before
		// our new child tries to start, and fails because a child already exists.  This situation gets cleaned up by the idle timeout eventually.
		// Only broken clients should have any issues, but we have to make sure they work also.
		
		destroyChild(clientMessage.player.id);
		Connection.removeConnection(clientMessage.player.id);
		
		
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		Connection connection = new Connection(netMessage.protocol, netMessage.ip, clientConnection, clientMessage.player.id,clientId);
		Connection.addConnection(clientMessage.player.id, connection);
		createChild(connection);
		RequestHandler.registerClient(clientMessage);
	}

	private void handleLogout(ClientMessage clientMessage, long clientId) {
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		logger.debug("PlayerLogout from " + clientMessage.getPlayer().getId());

		if (!Authentication.isAuthenticated(clientMessage.getPlayer())) {
			logger.debug("Unauthenticated client " + clientMessage.getPlayer().getId() + " attempting to logout");
			return;
		}

		if (Connection.hasConnection(clientMessage.player.id)) {
			destroyChild(clientMessage.player.id);
			Connection.removeConnection(clientMessage.player.id);
			RequestHandler.unregisterClient(clientMessage);
			PlayerService.getInstance().setAuthtoken(clientMessage.player.id, 0);
			PlayerService.getInstance().setCharacter(clientMessage.player.id, null);
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

	private ClientConnection createClientConnection(long clientId, ClientMessage clientMessage) {
		ClientConnection clientConnection = new ClientConnection();
		clientConnection.setId(Long.toString(clientId)).setGateway(Incoming.name).setServer("server");
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
