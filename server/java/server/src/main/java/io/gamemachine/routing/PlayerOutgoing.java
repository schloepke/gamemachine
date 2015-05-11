package io.gamemachine.routing;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.ClientConnection;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerConnected;
import io.gamemachine.net.Connection;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PlayerOutgoing extends UntypedActor {

		
	private Connection connection;
	private String playerId;
	private ClientConnection clientConnection;
	private int idleTimeout;
	private long lastActivity;
	private String gameId;
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public PlayerOutgoing(Connection connection) {
		this.connection = connection;
		this.playerId = connection.getPlayerId();
		this.gameId = PlayerService.getInstance().getGameId(playerId);
		this.clientConnection = connection.getClientConnection();
		this.idleTimeout = AppConfig.Client.getIdleTimeout();
		logger.debug("Player idle timeout = "+this.idleTimeout);
		this.lastActivity = System.currentTimeMillis() / 1000l;
		tick(1000l,"idle_timeout");
		
		sendConnectedMessage();
		logger.debug("Player gateway created for "+playerId);
	}
	
	private ClientMessage createClientMessage() {
		ClientMessage clientMessage = new ClientMessage();
		clientMessage.setClientConnection(clientConnection);
		return clientMessage;
	}
	
	private void sendConnectedMessage() {
		ClientMessage clientMessage = createClientMessage();
		clientMessage.setPlayerConnected(new PlayerConnected());
		sendToClient(clientMessage);
	}
	
	private void sendToClient(ClientMessage clientMessage) {
		connection.sendToClient(clientMessage);
	}
	
	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
	
	private void unregisterIfIdle() {
		if (((System.currentTimeMillis() / 1000l) - lastActivity) > idleTimeout) {
			unregister();
		}
		tick(1000l,"idle_timeout");
	}
	
	private void unregister() {
		logger.info("Player "+playerId+" timed out");
		ClientMessage clientMessage = createClientMessage();
		clientMessage.setPlayer(new Player().setId(playerId));
		Connection.removeConnection(playerId);
		RequestHandler.unregisterClient(clientMessage);
		PlayerService.getInstance().setAuthtoken(playerId, 0);
		PlayerService.getInstance().setCharacter(playerId, null);
		getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String)message;
			if (msg.equals("idle_timeout")) {
				unregisterIfIdle();
			} else if (msg.equals(this.gameId)) {
				unregister();
			}
		} else {
			lastActivity = System.currentTimeMillis() / 1000l;
			ClientMessage clientMessage = createClientMessage();
			Entity entity = (Entity)message;
			entity.setSendToPlayer(true);
			clientMessage.addEntity(entity);
			sendToClient(clientMessage);
		}
	}

}
