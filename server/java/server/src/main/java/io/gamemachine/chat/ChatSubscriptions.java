package io.gamemachine.chat;

import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.ActorUtil;


public class ChatSubscriptions extends UntypedActor {

	private static ConcurrentHashMap<String, String> playerGroups = new ConcurrentHashMap<String, String>();
	
	public static String name = ChatSubscriptions.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	public static class ChatEvent {
		public String action;
		public String chatId;
		public String channel;
		
		public ChatEvent(String action, String chatId, String channel) {
			this.action = action;
			this.chatId = chatId;
			this.channel = channel;
		}
	}
	
	public static void Subscribe(String chatId, String channel) {
		ActorSelection sel = ActorUtil.getSelectionByName(ChatSubscriptions.name);
		sel.tell(new ChatEvent("subscribe",chatId,channel), null);
	}

	public static void Unsubscribe(String chatId, String channel) {
		ActorSelection sel = ActorUtil.getSelectionByName(ChatSubscriptions.name);
		sel.tell(new ChatEvent("unsubscribe",chatId,channel), null);
	}
	
	public static String playerGroup(String playerId) {
		if (playerGroups.containsKey(playerId)) {
			return playerGroups.get(playerId);
		} else {
			return "nogroup";
		}
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		ChatEvent chatEvent = (ChatEvent)message;
		logger.warning(chatEvent.action+" "+chatEvent.chatId+" "+chatEvent.channel);
		
		if (chatEvent.action.equals("subscribe")) {
			if (chatEvent.channel.endsWith("group")) {
				playerGroups.put(chatEvent.chatId, chatEvent.channel);
			}
		} else if (chatEvent.action.equals("unsubscribe")) {
			if (chatEvent.channel.endsWith("group")) {
				playerGroups.remove(chatEvent.chatId);
			}
		}
	}
}
