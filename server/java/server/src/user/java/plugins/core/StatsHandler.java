package plugins.core;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.GameLimits;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmStats;

public class StatsHandler extends GameMessageActor {

    public static String name = StatsHandler.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void awake() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        if (exactlyOnce(gameMessage)) {
            GmStats stats = new GmStats();
            stats.playerBytesOut = GameLimits.getPlayerBpsOut(playerId);
            stats.messageCountInOut = GameLimits.getMpsInOut();
            stats.messageCountIn = GameLimits.getMpsIn();
            stats.messageCountOut = GameLimits.getMpsOut();
            stats.bytesOut = GameLimits.getBpsOut();
            stats.connectionCount = GameLimits.getConnectionCount();

            gameMessage.gmStats = stats;
            setReply(gameMessage);
        }

    }

}
