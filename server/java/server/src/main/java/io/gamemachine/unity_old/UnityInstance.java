package io.gamemachine.unity_old;

import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.UnityInstanceStatus;

public class UnityInstance {

    private final String clientActor = "UnityInstanceActor";
    public UnityInstanceStatus status;
    public boolean assigned = false;
    public boolean running = false;
    public long lastContact = 0L;

    public UnityInstance(UnityInstanceStatus status) {
        this.status = status;
    }

    public void release() {
        UnityInstanceManager.releaseInstance(this);
    }

    public boolean tell(GameMessage gameMessage, String actorName) {
        return UnityGameMessageHandler.tell(gameMessage, actorName, status.playerId);
    }

    public GameMessage ask(GameMessage gameMessage, String actorName) {
        return UnityGameMessageHandler.ask(gameMessage, actorName, status.playerId);
    }

    public GameMessage ask(GameMessage gameMessage, String actorName, int timeout) {
        return UnityGameMessageHandler.ask(gameMessage, actorName, status.playerId, timeout);
    }

    public GameMessage ask(GameMessage gameMessage, String actorName, int timeout, int attempts) {
        return UnityGameMessageHandler.ask(gameMessage, actorName, status.playerId, timeout, attempts);
    }

    public UnityInstanceStatus ask(UnityInstanceStatus instance) {
        GameMessage gameMessage = new GameMessage();
        gameMessage.unityInstanceStatus = instance;
        GameMessage response = ask(gameMessage, clientActor);
        if (response != null) {
            return response.unityInstanceStatus;
        } else {
            return null;
        }
    }
}
