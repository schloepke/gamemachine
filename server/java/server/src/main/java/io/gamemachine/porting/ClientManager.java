package io.gamemachine.porting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.Entity;

public class ClientManager extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);

    public Map<String, String> localPlayers = new ConcurrentHashMap<String, String>();
    public Map<String, String> localActors = new ConcurrentHashMap<String, String>();
    public Map<String, ActorRef> remoteClients = new ConcurrentHashMap<String, ActorRef>();
    public Map<String, String> localClients = new ConcurrentHashMap<String, String>();
    public Map<String, String> players = new ConcurrentHashMap<String, String>();
    public Map<String, String> clientToPlayer = new ConcurrentHashMap<String, String>();


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Entity) {
            Entity entity = (Entity) message;
            if (entity.sendToPlayer) {
                if (localPlayers.containsKey(entity.player.id)) {
                    ActorSelection sel = ActorUtil.getSelectionByName(entity.player.id);
                    sel.tell(entity, getSelf());
                } else {
                    sendToRemotePlayer(entity);
                }
            } else if (entity.clientEvent != null) {

            }
        }

    }

    private void sendToRemotePlayer(Entity entity) {
        logger.debug("Send to remote player " + entity.player.id);
        if (players.containsKey(entity.player.id)) {
            String clientId = players.get(entity.player.id);
            if (remoteClients.containsKey(clientId)) {
                ActorRef remoteRef = remoteClients.get(clientId);
                remoteRef.tell(entity, getSelf());
            }
        }
    }

}
