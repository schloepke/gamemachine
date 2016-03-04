package io.gamemachine.unity_old;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.UnityGameMessage;
import io.gamemachine.net.Connection;
import io.gamemachine.net.udp.NettyUdpServerHandler;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class UnityGameMessageHandler extends UntypedActor {

    private static Map<Long, ActorRef> pending = new ConcurrentHashMap<Long, ActorRef>();
    private static AtomicLong messageId = new AtomicLong();
    private static Timeout defaultTimeout = new Timeout(Duration.create(30, TimeUnit.MILLISECONDS));
    private static Map<Integer, Timeout> timeouts = new ConcurrentHashMap<Integer, Timeout>();
    private static Map<String, HashSet<String>> unityActors = new ConcurrentHashMap<String, HashSet<String>>();

    public static String name = UnityGameMessageHandler.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnityGameMessageHandler.class);
    private static ActorSelection sel;
    private static AskableActorSelection askable;

    private static Timeout getTimeout(int t) {
        if (!timeouts.containsKey(t)) {
            Timeout timeout = new Timeout(Duration.create(t, TimeUnit.MILLISECONDS));
            timeouts.put(t, timeout);
            return timeout;
        } else {
            return timeouts.get(t);
        }
    }

    public static boolean tell(GameMessage gameMessage, String actorName, String playerId) {
        if (sel == null) {
            return false;
        }

        if (Strings.isNullOrEmpty(playerId)) {
            return false;
        }
        UnityGameMessage unityGameMessage = createUnityGameMessage(gameMessage, actorName, playerId,
                UnityGameMessage.MessageType.Tell);

        if (unityGameMessage == null) {
            return false;
        }

        sel.tell(unityGameMessage, null);
        return true;
    }

    private static UnityGameMessage ask(UnityGameMessage message, Timeout timeout, int attempts) {
        if (askable == null) {
            return null;
        }

        Future<Object> future = askable.ask(message, timeout);
        for (int i = 0; i < attempts; i++) {
            try {
                Object result = Await.result(future, timeout.duration());
                return (UnityGameMessage) result;
            } catch (Exception e) {
                //return null;
            }
        }
        return null;
    }

    public static GameMessage ask(GameMessage gameMessage, String actorName, String playerId) {
        return ask(gameMessage, actorName, playerId, defaultTimeout, 1);
    }

    public static GameMessage ask(GameMessage gameMessage, String actorName, String playerId, int timeoutSeconds) {
        Timeout timeout = getTimeout(timeoutSeconds);
        return ask(gameMessage, actorName, playerId, timeout, 1);
    }

    public static GameMessage ask(GameMessage gameMessage, String actorName, String playerId, int timeoutSeconds, int attempts) {
        Timeout timeout = getTimeout(timeoutSeconds);
        return ask(gameMessage, actorName, playerId, timeout, attempts);
    }

    public static GameMessage ask(GameMessage gameMessage, String actorName, String playerId, Timeout timeout, int attempts) {
        if (Strings.isNullOrEmpty(playerId)) {
            return null;
        }
        UnityGameMessage unityGameMessage = createUnityGameMessage(gameMessage, actorName, playerId,
                UnityGameMessage.MessageType.Ask);

        if (unityGameMessage == null) {
            return null;
        }

        UnityGameMessage response = ask(unityGameMessage, timeout, attempts);
        if (response == null) {
            return null;
        } else {
            return response.gameMessage;
        }
    }

    public static boolean hasActor(String actorName, String playerId) {
        if (unityActors.containsKey(playerId)) {
            Set<String> actors = unityActors.get(playerId);
            if (actors.contains(actorName)) {
                return true;
            }
        }
        return false;
    }

    private static void ensureActors(String playerId) {
        if (!unityActors.containsKey(playerId)) {
            unityActors.put(playerId, new HashSet<String>());
        }
    }

    private static synchronized void removeActor(String actorName, String playerId) {
        ensureActors(playerId);
        unityActors.get(playerId).remove(actorName);
        logger.warn("removing unity actor " + actorName + " player id " + playerId);
    }

    private static synchronized void addActor(String actorName, String playerId) {
        ensureActors(playerId);
        unityActors.get(playerId).add(actorName);
        logger.warn("adding unity actor " + actorName + " player id " + playerId);
    }

    private static UnityGameMessage createUnityGameMessage(GameMessage gameMessage, String actorName, String playerId,
                                                           UnityGameMessage.MessageType messageType) {

        if (!hasActor(actorName, playerId)) {
            logger.debug("unity actor not found " + actorName + " for " + playerId);
            return null;
        }

        UnityGameMessage unityGameMessage = new UnityGameMessage();
        unityGameMessage.messageType = messageType;
        unityGameMessage.actorName = actorName;
        unityGameMessage.gameMessage = gameMessage;

        unityGameMessage.playerId = playerId;

        if (!Connection.hasConnection(playerId)) {
            logger.info("No connection found for playerId " + playerId);
            removeActor(actorName, playerId);
            return null;
        } else {
            return unityGameMessage;
        }
    }

    @Override
    public void preStart() {
        sel = ActorUtil.getSelectionByName(name);
        askable = new AskableActorSelection(sel);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        UnityGameMessage unityGameMessage = (UnityGameMessage) message;

        if (unityGameMessage.messageType == UnityGameMessage.MessageType.Register) {
            addActor(unityGameMessage.actorName, unityGameMessage.playerId);
            logger.info("Registered unity actor " + unityGameMessage.actorName + " for playerId "
                    + unityGameMessage.playerId);
            return;
        }

        if (unityGameMessage.messageId > 0) {
            if (unityGameMessage.messageType == UnityGameMessage.MessageType.Ask) {
                if (pending.containsKey(unityGameMessage.messageId)) {
                    ActorRef ref = pending.get(unityGameMessage.messageId);
                    ref.tell(unityGameMessage, getSelf());
                    pending.remove(unityGameMessage.messageId);
                } else {
                    logger.info("message id not found " + unityGameMessage.messageId);
                }
            } else {
                logger.info("Unity responded to tell " + unityGameMessage.messageId);
            }

        } else {

            ClientMessage clientMessage = new ClientMessage();
            clientMessage.unityGameMessage = unityGameMessage;

            Connection connection = Connection.getConnection(unityGameMessage.playerId);
            if (connection != null) {
                if (unityGameMessage.messageType == UnityGameMessage.MessageType.Ask) {
                    unityGameMessage.messageId = messageId.getAndIncrement();
                    pending.put(unityGameMessage.messageId, getSender());
                }

                NettyUdpServerHandler.sendMessage(connection.clientId, clientMessage.toByteArray());
            } else {
                logger.info("No connection found for playerId " + unityGameMessage.playerId);
                removeActor(unityGameMessage.actorName, unityGameMessage.playerId);
            }
        }
    }
}
