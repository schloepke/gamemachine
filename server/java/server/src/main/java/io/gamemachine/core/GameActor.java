package io.gamemachine.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;

import akka.actor.UntypedActor;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GameMessages;

public class GameActor extends UntypedActor {

    private ConcurrentHashMap<String, GameMessage> reliableMessages = new ConcurrentHashMap<String, GameMessage>();
    private ConcurrentHashMap<String, Integer> reliableMessageStatus = new ConcurrentHashMap<String, Integer>();
    private static Map<String, Long> idsToRemove = new ConcurrentHashMap<String, Long>();
    private long messageIdExpire = 5000L;

    public String playerId;
    public String characterId;
    public String messageId;

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
        if (this.playerId != null) {
            this.characterId = PlayerService.getInstance().getCharacterId(this.playerId);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }

    public void removeExpiredMessageIds() {
        for (String messageId : idsToRemove.keySet()) {
            Long updatedAt = idsToRemove.get(messageId);
            if ((System.currentTimeMillis() - updatedAt) > messageIdExpire) {
                if (reliableMessages.containsKey(messageId)) {
                    reliableMessages.remove(messageId);
                }

                if (reliableMessageStatus.containsKey(messageId)) {
                    reliableMessageStatus.remove(messageId);
                }

                idsToRemove.remove(messageId);
            }
        }

    }

    public boolean setReply(GameMessage gameMessage) {
        gameMessage.messageId = this.messageId;
        if (reliableMessageStatus.containsKey(gameMessage.messageId)) {
            reliableMessages.put(gameMessage.messageId, gameMessage);
            reliableMessageStatus.replace(gameMessage.messageId, 1);
            sendReply(gameMessage.messageId);
            return true;
        } else {
            return false;
        }
    }

    private void sendReply(String messageId) {
        GameMessage gameMessage = reliableMessages.get(messageId);
        Entity entity = new Entity();
        entity.setId(messageId);
        entity.setGameMessages(new GameMessages());
        entity.gameMessages.addGameMessage(gameMessage);
        PlayerMessage.tell(entity, playerId);
    }

    public boolean exactlyOnce(GameMessage gameMessage) {
        if (!Strings.isNullOrEmpty(gameMessage.messageId)) {
            idsToRemove.put(gameMessage.messageId, System.currentTimeMillis());
            if (reliableMessageStatus.containsKey(gameMessage.messageId)) {
                int status = reliableMessageStatus.get(gameMessage.messageId);
                if (status == 1) {
                    sendReply(gameMessage.messageId);
                }
            } else {
                reliableMessageStatus.put(gameMessage.messageId, 0);
                this.messageId = gameMessage.messageId;
                return true;
            }
        }
        return false;
    }
}
