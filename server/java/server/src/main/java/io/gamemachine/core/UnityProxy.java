package io.gamemachine.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PathData;
import io.gamemachine.messages.Vector3;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

public class UnityProxy extends GameMessageActor {

	public static String name = UnityProxy.class.getSimpleName();
	public static String unityPlayerId = "unity";
	
	private static AtomicLong counter = new AtomicLong();
	
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private ConcurrentHashMap<Long, ActorRef> actors = new ConcurrentHashMap<Long, ActorRef>();
	
	
	@Override
	public void awake() {
		PlayerService.getInstance().create(unityPlayerId, AppConfig.getDefaultGameId());
		logger.warning("UnityProxy awake");
		GameMessage msg = new GameMessage();
		msg.playerId = unityPlayerId;
		msg.pathData = new PathData();
		msg.pathData.startPoint = new Vector3();
		msg.pathData.endPoint = new Vector3();
		scheduleOnce(100,msg);
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		
		
		// response from unity
		if (gameMessage.hasUnityMessageId()) {
			if (actors.containsKey(gameMessage.unityMessageId)) {
				actors.get(gameMessage.unityMessageId).tell(gameMessage, getSelf());
				actors.remove(gameMessage.unityMessageId);
			}
			
		// outgoing to unity
		} else {
			long unityMessageId = nextId();
			gameMessage.unityMessageId = unityMessageId;
			PlayerCommands.sendGameMessage(gameMessage, playerId);
			ActorRef ref = this.getSender();
			if (ref != null) {
				actors.put(unityMessageId, ref);
			}
		}
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		
	}
	
	private static long nextId() {
		return counter.incrementAndGet();
	}
	
	
	public static GameMessage ask(GameMessage gameMessage, int timeout) {
		
		gameMessage.playerId = unityPlayerId;
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
		AskableActorSelection askable = new AskableActorSelection(sel);
		GameMessage message;
		Future<Object> future = askable.ask(gameMessage,t);
		try {
			message = (GameMessage) Await.result(future, t.duration());
		} catch (Exception e) {
			return null;
		}
		return message;
	}
	
	public static void tell(GameMessage gameMessage) {
		gameMessage.playerId = unityPlayerId;
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(gameMessage, null);
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub
		
	}

}
