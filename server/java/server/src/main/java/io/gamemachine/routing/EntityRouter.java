package io.gamemachine.routing;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.EntityTracking;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorSelection;

public class EntityRouter {

    private static final Logger logger = LoggerFactory.getLogger(EntityRouter.class);
    private Map<String, ActorSelection> destinations = new HashMap<String, ActorSelection>();

    final class PlayerDestination {
        public final Player player;
        public final String agent;

        public PlayerDestination(Player player, String agent) {
            this.player = player;
            this.agent = agent;
        }
    }

    private ActorSelection entityTracking;

    public EntityRouter() {
        entityTracking = ActorUtil.getSelectionByName(EntityTracking.name);
    }

    public void routeTrackData(TrackData trackData) {
        entityTracking.tell(trackData, null);
    }

    public void route(List<Entity> entities, Player player) {
        for (Entity entity : entities) {
            entity.setPlayer(player);

            if (entity.trackData != null || entity.trackDataUpdate != null || entity.agentTrackData != null) {
                entityTracking.tell(entity, null);
                continue;
            }

            if (!Strings.isNullOrEmpty(entity.destination)) {
                routeByDestination(entity);
                continue;
            }

            if (entity.gameMessages != null) {
                routeByGameMessage(entity);
            }

            routeByComponent(entity);
        }
    }

    private void routeByComponent(Entity entity) {
        ActorSelection sel;
        if (entity.echoTest != null) {
            sel = ActorUtil.getSelectionByName("GameMachine::GameSystems::RemoteEcho");
            sel.tell(entity, null);
        }
        if (entity.chatInvite != null || entity.chatMessage != null || entity.chatStatus != null || entity.leaveChat != null
                || entity.joinChat != null) {
            sel = ActorUtil.getSelectionByName("GameMachine::GameSystems::ChatManager");
            sel.tell(entity, null);
        }
    }

    private void routeByDestination(Entity entity) {
        String destination = entity.getDestination();
        if (destination == null) {
            logger.error("Destination is null");
            return;
        }

        PlayerDestination pd = destinationToPlayer(destination, entity.getPlayer().getId());
        if (pd != null) {
            Player recipientPlayer = new Player();
            recipientPlayer.setId(pd.player.getId());
            entity.setPlayer(recipientPlayer).setSenderId(entity.getPlayer().getId());
            PlayerMessage.tell(entity, recipientPlayer.getId());
            logger.debug("Message sent to player destination " + recipientPlayer.getId());
            return;
        }

        if (!destination.equals("GameMachine/GameSystems/RemoteEcho")
                && !destination.equals("GameMachine/GameSystems/Devnull")
                && !destination.equals("GameMachine/GameSystems/TeamManager")
                && !destination.equals("GameMachine/GameSystems/RegionService")) {
            logger.info("Bad destination " + destination);
            return;
        }

        String path = destination.replaceAll("/", "::");
        if (!destinations.containsKey(destination)) {
            ActorSelection sel = ActorUtil.getSelectionByName(path);
            destinations.put(path, sel);
        }
        entity.setDestination(null);
        ActorSelection dest = destinations.get(path);
        dest.tell(entity, null);
    }

    private void routeByGameMessage(Entity entity) {
        for (GameMessage gameMessage : entity.getGameMessages().getGameMessageList()) {
            if (!Strings.isNullOrEmpty(gameMessage.destination)) {
                String destination = gameMessage.getDestination();
                logger.debug("GameMessage destination " + destination);
                PlayerDestination pd = destinationToPlayer(destination, entity.getPlayer().getId());
                if (pd != null) {
                    Player recipientPlayer = pd.player;
                    if (pd.agent != null) {
                        gameMessage.setAgentId(pd.agent);
                    }
                    PlayerMessage.tell(gameMessage, recipientPlayer.getId());
                    logger.debug("GameMessage sent to " + recipientPlayer.getId());
                    continue;
                }
            } else {
                continue;
            }

            GameMessageRoute route = GameMessageRoute.routeFor(gameMessage);
            if (route != null) {
                gameMessage.setPlayerId(entity.getPlayer().getId());
                ActorSelection sel;
                logger.debug("GameMessage route to=" + route.to + " player=" + entity.getPlayer().getId());
                if (route.distributed) {
                    sel = ActorUtil.findDistributed(route.to, entity.getPlayer().getId());
                } else {
                    sel = ActorUtil.getSelectionByName(route.to);
                }
                sel.tell(gameMessage, null);
            }
        }
    }

    private PlayerDestination destinationToPlayer(String destination, String senderId) {
        if (destination == null || !destination.startsWith("player/")) {
            return null;
        }
        PlayerService playerService = PlayerService.getInstance();
        String[] parts = destination.split("/");
        Player recipientPlayer = playerService.find(parts[1]);
        if (recipientPlayer == null) {
            logger.debug("Destination player " + parts[1] + " not found");
        }
        if (recipientPlayer != null) {
            String playerGameId = playerService.getGameId(senderId);
            if (recipientPlayer.getGameId().equals(playerGameId)) {
                String agentId = null;
                if (parts.length == 3) {
                    agentId = parts[2];
                }
                PlayerDestination pd = new PlayerDestination(recipientPlayer, agentId);
                return pd;
            } else {
                logger.info("Destination player " + recipientPlayer.getId() + " game id does not match sender ");
            }
        }
        return null;
    }
}
