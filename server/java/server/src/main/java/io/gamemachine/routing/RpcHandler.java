package io.gamemachine.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.grid.GameGrid;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.RpcMessage;
import io.gamemachine.net.Connection;
import io.gamemachine.net.udp.NettyUdpServerHandler;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class RpcHandler extends UntypedActor {

	public static Map<Long,ActorRef> pending = new ConcurrentHashMap<Long,ActorRef>();
	public static AtomicLong messageId = new AtomicLong();
	public static Timeout t = new Timeout(Duration.create(100, TimeUnit.MILLISECONDS));
	
	public static String name = RpcHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public static void callRpc(RpcMessage message, String playerId, ActorRef sender) {
		if (!Connection.hasConnection(playerId)) {
			return;
		}
		message.playerId = playerId;
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(message,sender);
	}
	
	public static RpcMessage callRpc(RpcMessage message, String playerId) {
		if (!Connection.hasConnection(playerId)) {
			return null;
		}
		
		message.playerId = playerId;
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		AskableActorSelection askable = new AskableActorSelection(sel);
		Future<Object> future = askable.ask(message, t);
		try {
			Object result = Await.result(future, t.duration());
			return (RpcMessage)result;
		} catch (Exception e) {
			return null;
		}
	}

	public static String PlayerIdToRpcPlayerId(String playerId) {
		int zone = GameGrid.getEntityZone(playerId);
		return "zone"+zone;
	}
	
	public static GameMessage callRpc(String methodName, GameMessage gameMessage, String playerId) {
		RpcMessage rpcMessage = createRpc(methodName,gameMessage);
		RpcMessage response = callRpc(rpcMessage, playerId);
		if (response == null) {
			return null;
		} else {
			return response.gameMessage;
		}
	}
	
	public static RpcMessage createRpc(String methodName, GameMessage gameMessage) {
		RpcMessage msg = new RpcMessage();
		msg.method = methodName;
		msg.gameMessage = gameMessage;
		return msg;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		RpcMessage rpcMessage = (RpcMessage)message;
		
		if (rpcMessage.messageId > 0) {
			if (pending.containsKey(rpcMessage.messageId)) {
				ActorRef ref = pending.get(rpcMessage.messageId);
				ref.tell(rpcMessage,getSelf());
				pending.remove(rpcMessage.messageId);
			} else {
				logger.warning("message id not found "+rpcMessage.messageId);
			}
			
		} else {
			rpcMessage.messageId = messageId.getAndIncrement();
			ClientMessage clientMessage = new ClientMessage();
			clientMessage.rpcMessage = rpcMessage;
			Connection connection = Connection.getConnection(rpcMessage.playerId);
			if (connection != null) {
				pending.put(rpcMessage.messageId,getSender());
				NettyUdpServerHandler.sendMessage(connection.clientId, clientMessage.toByteArray());
			}
		}
	}
}
