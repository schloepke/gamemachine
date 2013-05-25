package com.game_machine.core.game;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.game_machine.core.ActorUtil;
import com.game_machine.core.GameMessage;
import com.game_machine.entity_system.Component;
import com.game_machine.entity_system.generated.ClientConnection;

public class ClientRegistry extends GameActor {

	private HashMap<String, Long> clients = new HashMap<String, Long>();

	public ClientRegistry() {
		this.getContext()
				.system()
				.scheduler()
				.schedule(Duration.Zero(), Duration.create(1000, TimeUnit.MILLISECONDS), this.getSelf(),
						"checkExpired", this.getContext().system().dispatcher(), null);
	}

	public void onReceive(String command) {
		//log.info("CheckExpired");
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

	public static ClientConnection getClientConnection(String clientId, Boolean connected) {
		return new ClientConnection().setId(clientId).setConnected(connected);
	}

	public static void register(String clientId) {
		GameMessage gameMessage = new GameMessage(clientId, null, null, getClientConnection(clientId, true));
		ActorUtil.getSelectionByName("ClientRegistry").tell(gameMessage, null);
	}

	public static void unregister(String clientId) {
		GameMessage gameMessage = new GameMessage(clientId, null, null, getClientConnection(clientId, false));
		ActorUtil.getSelectionByName("ClientRegistry").tell(gameMessage, null);
	}
}
