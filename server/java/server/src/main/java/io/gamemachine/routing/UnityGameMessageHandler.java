package io.gamemachine.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public static Map<Long,ActorRef> pending = new ConcurrentHashMap<Long,ActorRef>();
	public static AtomicLong messageId = new AtomicLong();
	public static Timeout t = new Timeout(Duration.create(100, TimeUnit.MILLISECONDS));
	public static Map<String,String> unityActors = new ConcurrentHashMap<String,String>();
	
	public static String name = UnityGameMessageHandler.class.getSimpleName();
	private static final Logger logger = LoggerFactory.getLogger(UnityGameMessageHandler.class);
	
	private static UnityGameMessage ask(UnityGameMessage message) {
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		AskableActorSelection askable = new AskableActorSelection(sel);
		Future<Object> future = askable.ask(message, t);
		try {
			Object result = Await.result(future, t.duration());
			return (UnityGameMessage)result;
		} catch (Exception e) {
			return null;
		}
	}

	public static void tell(GameMessage gameMessage, String actorName) {
		UnityGameMessage unityGameMessage = createUnityGameMessage(gameMessage,actorName, UnityGameMessage.MessageType.Tell);
		if (unityGameMessage == null) {
			return;
		}
		
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(unityGameMessage,null);
	}
	
	public static GameMessage ask(GameMessage gameMessage, String actorName) {
		UnityGameMessage unityGameMessage = createUnityGameMessage(gameMessage, actorName, UnityGameMessage.MessageType.Ask);
		if (unityGameMessage == null) {
			return null;
		}
		
		UnityGameMessage response = ask(unityGameMessage);
		if (response == null) {
			return null;
		} else {
			return response.gameMessage;
		}
	}
	
	public static boolean hasGameActor(String actorName) {
		if (unityActors.containsKey(actorName)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static UnityGameMessage createUnityGameMessage(GameMessage gameMessage,String actorName, UnityGameMessage.MessageType messageType) {
		if (!hasGameActor(actorName)) {
			logger.warn("unity actor not found "+actorName);
			return null;
		}
		
		UnityGameMessage unityGameMessage = new UnityGameMessage();
		unityGameMessage.messageType = messageType;
		unityGameMessage.actorName = actorName;
		unityGameMessage.gameMessage = gameMessage;
		
		String playerId = unityActors.get(actorName);
		unityGameMessage.playerId = playerId;
		
		if (!Connection.hasConnection(playerId)) {
			logger.warn("No connection found for playerId "+playerId);
			removeUnityActor(actorName);
			return null;
		} else {
			return unityGameMessage;
		}
	}
	
	private static void removeUnityActor(String actorName) {
		if (!hasGameActor(actorName)) {
			logger.warn("unity actor not found "+actorName);
			return;
		}
		
		String playerId = unityActors.get(actorName);
		logger.warn("removing unity actor "+actorName+" player id "+playerId);
		unityActors.remove(actorName);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		UnityGameMessage unityGameMessage = (UnityGameMessage)message;
		
		if (unityGameMessage.messageType == UnityGameMessage.MessageType.Register) {
			unityActors.put(unityGameMessage.actorName, unityGameMessage.playerId);
			logger.warn("Registered unity actor "+unityGameMessage.actorName+" for playerId "+unityGameMessage.playerId);
			return;
		}
		
		if (unityGameMessage.messageId > 0) {
			if (unityGameMessage.messageType == UnityGameMessage.MessageType.Ask) {
				if (pending.containsKey(unityGameMessage.messageId)) {
					ActorRef ref = pending.get(unityGameMessage.messageId);
					ref.tell(unityGameMessage,getSelf());
					pending.remove(unityGameMessage.messageId);
				} else {
					logger.warn("message id not found "+unityGameMessage.messageId);
				}
			} else {
				logger.warn("Unity responded to tell "+unityGameMessage.messageId);
			}
			
			
		} else {
			
			ClientMessage clientMessage = new ClientMessage();
			clientMessage.unityGameMessage = unityGameMessage;
			
			Connection connection = Connection.getConnection(unityGameMessage.playerId);
			if (connection != null) {
				if (unityGameMessage.messageType == UnityGameMessage.MessageType.Ask) {
					unityGameMessage.messageId = messageId.getAndIncrement();
					pending.put(unityGameMessage.messageId,getSender());
				}
				
				NettyUdpServerHandler.sendMessage(connection.clientId, clientMessage.toByteArray());
			} else {
				logger.warn("No connection found for playerId "+unityGameMessage.playerId);
				removeUnityActor(unityGameMessage.actorName);
			}
		}
	}
}
