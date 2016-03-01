package io.gamemachine.core;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

public class CacheUpdateHandler extends UntypedActor {

    private static HashMap<Class<?>, Method> methods = new HashMap<Class<?>, Method>();
    private static final Logger log = LoggerFactory.getLogger(CacheUpdateHandler.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CacheUpdate) {
            CacheUpdate cacheUpdate = (CacheUpdate) message;
            Class<?> klass = cacheUpdate.getKlass();
            if (!methods.containsKey(klass)) {
                methods.put(klass, klass.getMethod("setFromUpdate", CacheUpdate.class));
            }
            Method method = methods.get(klass);
            Object result = method.invoke(null, cacheUpdate);
            if (getSender() != null) {
                getSender().tell(result, null);
            }
        }
    }

}
