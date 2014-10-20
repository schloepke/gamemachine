package com.game_machine.objectdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;

import com.game_machine.core.PersistableMessage;

public class WriteBehindCache extends UntypedActor {

	private long cacheWritesPerSecond;
	private long writeDelay;
	private long cacheWriteInterval;
	private long lastWrite;
	private Store store;
	private BlockingQueue<DelayElement> queue = new DelayQueue<DelayElement>();
	private HashMap<String, PersistableMessage> cache = new HashMap<String, PersistableMessage>();

	public WriteBehindCache(Store store) {
		this.store = store;
		this.cacheWritesPerSecond = store.getCacheWritesPerSecond();
		this.cacheWriteInterval = store.getCacheWriteInterval();
		this.writeDelay = 1000l / cacheWritesPerSecond;
		this.lastWrite = System.currentTimeMillis() - (120 * 1000);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String) message;
			if (msg.equals("update")) {
				runQueue();
			}
		} else {
			PersistableMessage persistableMessage = (PersistableMessage) message;
			setMessage(persistableMessage);
			enqueue(persistableMessage);
			runQueue();
		}
	}

	private void runQueue() {
		if (!canWrite()) {
			return;
		}
		
		int size = (int) ((System.currentTimeMillis() - lastWrite) / writeDelay);
		final Collection<DelayElement> expired = new ArrayList<DelayElement>();
		queue.drainTo(expired,size);
		for (DelayElement element : expired) {
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
		lastWrite = System.currentTimeMillis() / 1000l;
	}

	private void enqueue(PersistableMessage message) {
		DelayElement element = new DelayElement(message.getId(), cacheWriteInterval);
		if (!queue.contains(element)) {
			queue.add(element);
		}
	}

	private void setMessage(PersistableMessage message) {
		cache.put(message.getId(), message);
	}

	@Override
	public void preStart() {
		tick(500l, "update");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
	

}
