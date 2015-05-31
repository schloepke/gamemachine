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
import io.gamemachine.messages.PlayerConnected;
import io.gamemachine.net.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Incoming extends UntypedActor {

	private static Map<String,Long> connectAttempts = new ConcurrentHashMap<String,Long>();
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
				logger.warning("Ignoring message before connection setup");
				return;
			}
		}

		if (clientMessage.hasPlayer()) {
			String gameId = playerService.getGameId(clientMessage.player.id);
			GameLimits.incrementMessageCountIn(gameId);
			
			if (!Authentication.hasValidAuthtoken(clientMessage.getPlayer())) {
				logger.warning("Player not authenticated " + clientMessage.getPlayer().getId() + " authtoken="
						+ clientMessage.getPlayer().getAuthtoken());
				return;
			}
			gameHandler.tell(clientMessage, getSelf());
		} else {
			logger.warning("No player");
		}

	}

	private void handleConnect(NetMessage netMessage, ClientMessage clientMessage, long clientId) {
		logger.info("PlayerConnect from " + clientMessage.getPlayer().getId());

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

		if (connectAttempts.containsKey(clientMessage.player.id)) {
			Long last = connectAttempts.get(clientMessage.player.id);
			if (System.currentTimeMillis() - last < 2000l) {
				logger.info("duplicate connect attempt too fast for " + clientMessage.player.id);
				return;
			}
		}
		
		connectAttempts.put(clientMessage.player.id, System.currentTimeMillis());
		
		if (Connection.hasConnection(clientMessage.player.id)) {
			Connection existing = Connection.getConnection(clientMessage.player.id);
			ClientMessage out = new ClientMessage();
			Player player = new Player();
			player.id = clientMessage.player.id;
			out.player = player;
			out.setClientConnection(existing.getClientConnection());
			out.setPlayerConnected(new PlayerConnected());
			existing.sendToClient(out);
			logger.info("Resending PlayerConnected to " + clientMessage.player.id);
			return;
		}
			
		
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		Connection connection = new Connection(netMessage.protocol, netMessage.ip, clientConnection, clientMessage.player.id,clientId);
		Connection.addConnection(clientMessage.player.id, connection);
		createChild(connection);
		RequestHandler.registerClient(clientMessage);
		logger.warning("Player registered " + clientMessage.player.id);
	}

	private void handleLogout(ClientMessage clientMessage, long clientId) {
		ClientConnection clientConnection = createClientConnection(clientId, clientMessage);
		clientMessage.setClientConnection(clientConnection);

		logger.debug("PlayerLogout from " + clientMessage.getPlayer().getId());

		if (!Authentication.hasValidAuthtoken(clientMessage.getPlayer())) {
			logger.warning("Unauthenticated client " + clientMessage.getPlayer().getId() + " attempting to logout");
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
		logger.info("Starting Outgoing actor " + connection.getPlayerId());
	}

	private void destroyChild(String playerId) {
		ActorSelection sel = ActorUtil.getSelectionByName(playerId);
		//sel.tell(akka.actor.PoisonPill.getInstance(), getSelf());
		String gameId = playerService.getGameId(playerId);
		sel.tell("die", getSelf());
		GameLimits.decrementConnectionCount(gameId);
		logger.info("Stopping Outgoing actor " + playerId);
	}



}
