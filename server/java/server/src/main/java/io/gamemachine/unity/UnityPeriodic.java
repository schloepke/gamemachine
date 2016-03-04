package io.gamemachine.unity;

import akka.actor.UntypedActor;
import io.gamemachine.messages.SyncObject;
import io.gamemachine.unity.unity_engine.UnityEngineHandler;
import io.gamemachine.unity.unity_engine.RegionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by chris on 3/1/2016.
 */
public class UnityPeriodic extends UntypedActor {

    public static String name = UnityPeriodic.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnityPeriodic.class);

    private long checkAliveInterval = 5000L;
    private long checkExpiredInterval = 2000L;
    private static long expireTime = 3000L;

    public UnityPeriodic() {

        scheduleOnce(checkAliveInterval,"updateAlive");
        scheduleOnce(checkExpiredInterval, "checkExpired");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            String command = (String)message;
            if (command.equals("updateAlive")) {
                updateAlive();
                scheduleOnce(checkAliveInterval,"updateAlive");
            } else if (command.equals("checkExpired")) {
                checkExpired();
                scheduleOnce(checkExpiredInterval, "checkExpired");
            }
        }
    }

    private void checkExpired() {
        for (SyncObject syncObject : UnitySync.syncObjects.values()) {
            if (System.currentTimeMillis() - syncObject.lastUpdate > expireTime) {
                UnitySync.syncObjects.remove(syncObject.id);
                UnityEngineHandler handler = UnitySync.handlers.get(syncObject.id);
                if (handler == null) {
                    logger.warn("Handler not found for syncObject "+syncObject.id);
                } else {
                    handler.componentRemoved(syncObject.id);
                    UnitySync.handlers.remove(syncObject.id);
                }
            }
        }
    }

    private void updateAlive() {
        for (String key : UnityMessageHandler.regions.keySet()) {
            RegionData data = UnityMessageHandler.regions.get(key);
            if (System.currentTimeMillis() - data.lastUpdate > 5000L) {
                UnityMessageHandler.regions.remove(key);
            }
        }
    }

    public final void scheduleOnce(long delay, String message) {
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
                        null);
    }
}
