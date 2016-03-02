package io.gamemachine.objectdb;

import io.gamemachine.core.EntitySerializer;
import io.gamemachine.core.PersistableMessage;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Store {

    private static final Logger logger = LoggerFactory.getLogger(Store.class);

    public static final AtomicInteger setCount = new AtomicInteger();
    public static final AtomicInteger getCount = new AtomicInteger();
    public static final AtomicInteger deleteCount = new AtomicInteger();
    private static HashMap<String, Class<?>> classCache = new HashMap<String, Class<?>>();
    private Storable store;
    private String serialization;

    private Store() {

    }

    private static class LazyHolder {
        private static final Store INSTANCE = new Store();
    }

    public static Store getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void connect(String storeName, String serialization) {
        this.serialization = serialization;
        logger.info("Entity serialization is " + serialization);

        if (storeName.equals("jdbc")) {
            this.store = (Storable) new JdbcStore();
            this.store.connect();
        } else if (storeName.equals("memory")) {
            this.store = (Storable) new MemoryStore();
            this.store.connect();
        }
    }

    public static Class<?> getKlass(String classname) throws ClassNotFoundException {
        if (classCache.containsKey(classname)) {
            return classCache.get(classname);
        } else {
            Class<?> clazz = Class.forName("io.gamemachine.messages." + classname);
            classCache.put(classname, clazz);
            return clazz;
        }
    }

    public Object get(String id) throws ClassNotFoundException {
        return get(id, "Entity");
    }

    public Object get(String id, Class<?> clazz) {
        getCount.incrementAndGet();
        if (serialization.equals("json")) {
            String stringValue = this.store.getString(id);
            if (stringValue == null) {
                return null;
            }
            return EntitySerializer.fromJson(stringValue, clazz);
        } else {
            byte[] byteValue = this.store.getBytes(id);
            if (byteValue == null) {
                return null;
            }
            return EntitySerializer.fromByteArray(byteValue, clazz);
        }
    }

    public Object get(String id, String classname) throws ClassNotFoundException {
        getCount.incrementAndGet();
        Class<?> clazz = getKlass(classname);
        if (serialization.equals("json")) {
            String stringValue = this.store.getString(id);
            if (stringValue == null) {
                return null;
            }
            return EntitySerializer.fromJson(stringValue, clazz);
        } else {
            byte[] byteValue = this.store.getBytes(id);
            if (byteValue == null) {
                return null;
            }
            return EntitySerializer.fromByteArray(byteValue, clazz);
        }
    }

    public void delete(String id) {
        this.store.delete(id);
        deleteCount.incrementAndGet();
    }

    public void set(String id, PersistableMessage message) {
        setCount.incrementAndGet();
        if (serialization.equals("json")) {
            this.store.setString(id, message.toJson());
        } else {
            this.store.setBytes(id, message.toByteArray());
        }
    }

    public void shutdown() {
        this.store.shutdown();
    }

}
