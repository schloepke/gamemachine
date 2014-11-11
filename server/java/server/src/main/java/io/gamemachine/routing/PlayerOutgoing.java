package io.gamemachine.routing;

import io.gamemachine.config.AppConfig;
import io.gamemachine.config.GameLimits;
import io.gamemachine.core.PlayerService;
import io.gamemachine.net.Connection;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.ClientConnection;
import GameMachine.Messages.ClientMessage;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Player;
import GameMachine.Messages.PlayerConnected;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PlayerOutgoing extends UntypedActor {

		
	private Connection connection;
	private String playerId;
	private ClientConnection clientConnection;
	private int idleTimeout;
	private long lastActivity;
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public PlayerOutgoing(Connection connection) {
		this.connection = connection;
		this.playerId = connection.getPlayerId();
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
			logger.info("Player "+playerId+" timed out");
			ClientMessage clientMessage = createClientMessage();
			clientMessage.setPlayer(new Player().setId(playerId));
			Incoming.removeClient(playerId);
			RequestHandler.unregisterClient(clientMessage);
			getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
		}
		tick(1000l,"idle_timeout");
	}
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (((String)message).equals("idle_timeout")) {
				unregisterIfIdle();
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
