package com.game_machine.core.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.game_machine.core.GameMachineConfig;
import com.game_machine.entity_system.generated.Entity;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRouter;

public class WriteBehindHandler extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private Integer writeInterval = 5000;
	private Integer maxWritesPerSecond = 50;
	private Integer minTimeBetweenWrites = 1000 / maxWritesPerSecond;
	private Long lastWrite = System.currentTimeMillis() - 10000;
	private HashMap<String, Entity> entities = new HashMap<String, Entity>();
	private HashMap<String, Long> entityUpdates = new HashMap<String, Long>();
	private ArrayList<Entity> queue = new ArrayList<Entity>();
	private HashMap<String, Integer> queueIndex = new HashMap<String, Integer>();
	private Entity currentEntity = null;

	public void onReceive(Object message) {
		if (message instanceof Entity) {
			write(message);
		} else if (message instanceof String) {
			if (message.equals("tick")) {
				checkQueue();
			}
		} else {
			unhandled(message);
		}
	}

	public Integer getMinTimeBetweenWrites() {
		return this.minTimeBetweenWrites;
	}

	public WriteBehindHandler(Integer writeInterval, Integer maxWritesPerSecond) throws ClassNotFoundException {
		this.writeInterval = writeInterval;
		this.maxWritesPerSecond = maxWritesPerSecond;

		Class<?> store = Class.forName(GameMachineConfig.objectStore);
		ActorRef storeRef = this.getContext().actorOf(Props.create(store).withRouter(new RoundRobinRouter(10)),
				store.getSimpleName());

		this.getContext()
				.system()
				.scheduler()
				.schedule(Duration.Zero(), Duration.create(this.getMinTimeBetweenWrites(), TimeUnit.MILLISECONDS),
						this.getSelf(), "tick", this.getContext().system().dispatcher(), null);
	}

	public Boolean writeEntity(Entity entity) {
		this.lastWrite = System.currentTimeMillis();
		return true;
	}

	public Boolean eligibleForWrite(Entity gameObject) {

		Long lastUpdated = entityUpdates.get(gameObject.getId());

		// No lastUpdated means was put in the queue on the first try and was
		// never written
		if (lastUpdated == null) {
			return true;
		}

		if ((System.currentTimeMillis() - lastUpdated) < writeInterval) {
			// Don't update a specific object more then once every writeInterval
			return false;
		} else {
			return true;
		}
	}

	public Boolean busy() {
		if ((System.currentTimeMillis() - lastWrite) < minTimeBetweenWrites) {
			return true;
		} else {
			return false;
		}
	}

	public void setEntity(Entity entity) {
		entities.put(entity.getId(), entity);
		entityUpdates.put(entity.getId(), System.currentTimeMillis());
	}

	public Boolean checkQueue() {
		if (queue.size() == 0) {
			return false;
		}
		currentEntity = queue.get(queue.size() - 1);
		if (!busy() && eligibleForWrite(currentEntity)) {
			if (writeEntity(currentEntity)) {
				entityUpdates.put(currentEntity.getId(), System.currentTimeMillis());
				queue.remove(queue.size() - 1);
				queueIndex.remove(currentEntity.getId());
				return true;
			}
		}
		return false;
	}

	public Boolean write(Object message) {
		currentEntity = (Entity) message;
		Boolean writeThrough = true;

		if (entities.containsKey(currentEntity.getId())) {
			if (!busy() && eligibleForWrite(currentEntity)) {
				writeThrough = true;
			} else {
				writeThrough = false;
			}
		} else if (busy()) {
			writeThrough = false;
		}

		entities.put(currentEntity.getId(), currentEntity);
		if (writeThrough) {
			if (writeEntity(currentEntity)) {
				entityUpdates.put(currentEntity.getId(), System.currentTimeMillis());
				return writeThrough;
			}
		} else {
			// queue for later write
			if (!queueIndex.containsKey(currentEntity.getId())) {
				queue.add(0, currentEntity);
			}
		}
		return writeThrough;
	}
}
