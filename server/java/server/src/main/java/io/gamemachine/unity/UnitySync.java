package io.gamemachine.unity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.gamemachine.unity.unity_engine.UnityEngineHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import io.gamemachine.messages.SyncObject;
import io.gamemachine.messages.SyncObjects;
import scala.concurrent.duration.Duration;

public class UnitySync extends UntypedActor {

    public static String name = UnitySync.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnitySync.class);
    private static long expireTime = 3L;
    private static Map<SyncObject.Type, Field> fields = new ConcurrentHashMap<SyncObject.Type, Field>();

    private static Map<String, SyncObject> syncObjects = new ConcurrentHashMap<String, SyncObject>();

    private static Map<String, UnityEngineHandler> handlers = new ConcurrentHashMap<String, UnityEngineHandler>();

    public UnitySync() {
        scheduleOnce(2000L, "checkExpired");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            checkExpired();
            return;
        }

        if (message instanceof SyncObjects) {
            SyncObjects syncObjects = (SyncObjects) message;
            for (SyncObject syncObject : syncObjects.syncObject) {
                set(syncObject);

            }
        }
    }

    public static void registerHandler(UnityEngineHandler handler, String id) {
        handlers.put(id,handler);
    }

    public static Object get(String id) {
        if (!syncObjects.containsKey(id)) {
            return null;
        }
        SyncObject syncObject = syncObjects.get(id);

        try {
            Field field = getField(syncObject.type);
            return field.get(syncObject);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            logger.warn("getAttribute error " + syncObject.type.toString() + " " + e.getMessage() + " returning null");
            return null;
        }
    }

    private void checkExpired() {
        for (SyncObject syncObject : syncObjects.values()) {
            if (System.currentTimeMillis() - syncObject.lastUpdate > 2000) {
                syncObjects.remove(syncObject.id);
                UnityEngineHandler handler = handlers.get(syncObject.id);
                if (handler == null) {
                    logger.warn("Handler not found for syncObject "+syncObject.id);
                } else {
                    handler.componentRemoved(syncObject.id);
                    handlers.remove(syncObject.id);
                }

            }
        }
        scheduleOnce(2000L, "checkExpired");
    }

    private void set(SyncObject syncObject) {
        syncObject.lastUpdate = System.currentTimeMillis();

        UnityEngineHandler handler = handlers.get(syncObject.id);
        if (handler != null) {
            if (!syncObjects.containsKey(syncObject.id)) {
                handler.componentAdded(syncObject);
            } else {
                handler.componentUpdated(syncObject);
            }
        }

        syncObjects.put(syncObject.id, syncObject);
    }

    private static Field getField(SyncObject.Type type) {
        if (fields.containsKey(type)) {
            return fields.get(type);
        } else {
            Field field;
            try {
                field = SyncObject.class.getField(type.toString());
                fields.put(type, field);
                return field;
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException("getField error " + e.getMessage());
            }
        }
    }

    private final void scheduleOnce(long delay, String message) {
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message, getContext().dispatcher(),
                        null);
    }
}
