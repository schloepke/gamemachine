package io.gamemachine.objectdb;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.PersistableMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;

public class WriteBehindCache extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(WriteBehindCache.class);
    public static final AtomicInteger queueSize = new AtomicInteger();
    private long cacheWritesPerSecond;
    private long writeDelay;
    private long cacheWriteInterval;
    private long lastWrite;
    private Store store;
    private BlockingQueue<DelayElement> queue = new DelayQueue<DelayElement>();
    private HashMap<String, PersistableMessage> cache = new HashMap<String, PersistableMessage>();
    final Collection<DelayElement> expired = new ArrayList<DelayElement>();

    public WriteBehindCache(Store store) {
        this.store = store;
        setConfig();
        this.lastWrite = System.currentTimeMillis() - (120 * 1000);
    }

    private void setConfig() {
        this.cacheWritesPerSecond = AppConfig.Datastore.getCacheWritesPerSecond();
        this.cacheWriteInterval = AppConfig.Datastore.getCacheWriteInterval();
        this.writeDelay = 1000l / this.cacheWritesPerSecond;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String msg = (String) message;
            if (msg.equals("update")) {
                runQueue();
                tick(500l, "update");
            } else if (msg.equals("maintenance")) {
                stats();
                setConfig();
                tick(5000l, "maintenance");
            }
        } else {
            PersistableMessage persistableMessage = (PersistableMessage) message;
            setMessage(persistableMessage);
            enqueue(persistableMessage);
            runQueue();
        }
    }

    private void stats() {
        //System.out.println("Queue size "+queue.size());
    }

    private void runQueue() {
        if (!canWrite()) {
            return;
        }

        int elapsed = (int) (System.currentTimeMillis() - lastWrite);
        if (elapsed < writeDelay) {
            return;
        }

        int size = (int) (elapsed / writeDelay);
        expired.clear();
        queue.drainTo(expired, size);
        for (DelayElement element : expired) {
            queueSize.decrementAndGet();
            String id = element.getElement();
            PersistableMessage message = cache.get(id);
            write(message);
        }
    }

    private boolean canWrite() {
        if ((System.currentTimeMillis() - lastWrite) >= writeDelay) {
            return true;
        } else {
            return false;
        }
    }

    private void write(PersistableMessage message) {
        store.set(message.getId(), message);
        cache.remove(message.getId());
        lastWrite = System.currentTimeMillis();
    }

    private void enqueue(PersistableMessage message) {
        DelayElement element = new DelayElement(message.getId(), cacheWriteInterval);
        if (!queue.contains(element)) {
            queue.add(element);
            queueSize.incrementAndGet();
        }
    }

    private void setMessage(PersistableMessage message) {
        cache.put(message.getId(), message);
    }

    @Override
    public void preStart() {
        logger.debug("cacheWritesPerSecond " + this.cacheWritesPerSecond);
        logger.debug("cacheWriteInterval " + this.cacheWriteInterval);
        logger.debug("writeDelay " + this.writeDelay);
        tick(500l, "update");
        tick(10000l, "maintenance");
    }

    public void tick(long delay, String message) {
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
                        getContext().dispatcher(), null);
    }


}
