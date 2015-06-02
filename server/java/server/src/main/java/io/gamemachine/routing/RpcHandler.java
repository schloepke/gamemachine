package io.gamemachine.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.ClientMessage;
import io.gamemachine.messages.RpcMessage;
import io.gamemachine.net.Connection;
import io.gamemachine.net.udp.NettyUdpServerHandler;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

public class RpcHandler extends UntypedActor {

	public static Map<Long,ActorRef> pending = new ConcurrentHashMap<Long,ActorRef>();
	public static AtomicLong messageId = new AtomicLong();
	public static Timeout t = new Timeout(Duration.create(100, TimeUnit.MILLISECONDS));
	
	public static String name = RpcHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public static void call(RpcMessage message, String playerId, ActorRef sender) {
		message.playerId = playerId;
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(message,sender);
	}
	
	public static RpcMessage call(RpcMessage message, String playerId) {
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

	@Override
	public void onReceive(Object message) throws Exception {
		RpcMessage rpcMessage = (RpcMessage)message;
		
		if (rpcMessage.hasMessageId()) {
			ActorRef ref = pending.get(rpcMessage.messageId);
			ref.tell(rpcMessage,getSelf());
			pending.remove(rpcMessage.messageId);
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
