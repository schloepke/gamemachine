package com.game_machine.core.game;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.entity_system.Component;
import com.game_machine.entity_system.generated.ClientConnection;
import com.game_machine.entity_system.generated.Entity;

public class ClientRegistry extends GameActor {

	private HashMap<String,Long> clients = new HashMap<String,Long>();
	
	public ClientRegistry() {
		this.getContext()
		.system()
		.scheduler()
		.schedule(
				Duration.Zero(),
				Duration.create(1000,
						TimeUnit.MILLISECONDS), this.getSelf(), "checkExpired",
				this.getContext().system().dispatcher(), null);
	}
	
	public void onReceive(String command) {
		log.info("CheckExpired");
	}
	
	public void onReceive(Component component) {
		ClientConnection clientConnection = (ClientConnection) component;
		if (clientConnection.getConnected() == true) {
			clients.put(clientConnection.getId(), currentTime());
		} else {
			clients.remove(clientConnection.getId());
		}
	}
	
	private long currentTime() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static void setConnected(String clientId) {
		ClientConnection clientConnection = new ClientConnection();
		clientConnection.setId(clientId);
		clientConnection.setConnected(true);
		GameMessage gameMessage = new GameMessage(clientId,null,null,clientConnection);
		ActorUtil.getSelectionByName("ClientRegistry").tell(gameMessage,null);
	}
}
