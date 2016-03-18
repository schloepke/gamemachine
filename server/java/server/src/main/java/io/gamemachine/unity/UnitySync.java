package io.gamemachine.unity;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import io.gamemachine.unity.unity_engine.HandlerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import io.gamemachine.messages.SyncObject;
import io.gamemachine.messages.SyncObjects;
import scala.concurrent.duration.Duration;

public class UnitySync extends UntypedActor {

    public static String name = UnitySync.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnitySync.class);

    private static Map<SyncObject.Type, Field> fields = new ConcurrentHashMap<SyncObject.Type, Field>();

    public static Map<String, SyncObject> syncObjects = new ConcurrentHashMap<String, SyncObject>();

    public static Map<String, ActorRef> handlers = new ConcurrentHashMap<String, ActorRef>();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SyncObjects) {
            SyncObjects syncObjects = (SyncObjects) message;
            for (SyncObject syncObject : syncObjects.syncObject) {
                set(syncObject);
            }
        }
    }

    public static void registerHandler(ActorRef handler, String id) {
        handlers.put(id,handler);
    }

    public static void unregisterHandler(String id) {
        handlers.remove(id);
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



    private void set(SyncObject syncObject) {
        syncObject.lastUpdate = System.currentTimeMillis();

        ActorRef handler = handlers.get(syncObject.id);
        if (handler == null) {
            logger.warn("No handler for "+syncObject.id);
            return;
        }

        if (!syncObjects.containsKey(syncObject.id)) {
            HandlerMessage handlerMessage = new HandlerMessage(HandlerMessage.Type.ComponentAdd);
            handlerMessage.message = syncObject;
            handler.tell(handlerMessage,getSelf());
        } else {
            HandlerMessage handlerMessage = new HandlerMessage(HandlerMessage.Type.ComponentUpdate);
            handlerMessage.message = syncObject;
            handler.tell(handlerMessage,getSelf());
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
