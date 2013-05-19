package com.game_machine.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.game_machine.Config;

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
	private HashMap<String, GameObject> gameObjects = new HashMap<String, GameObject>();
	private HashMap<String, Long> gameObjectUpdates = new HashMap<String, Long>();
	private ArrayList<GameObject> queue = new ArrayList<GameObject>();
	private HashMap<String, Integer> queueIndex = new HashMap<String, Integer>();
	private GameObject currentGameObject = null;

	public void onReceive(Object message) {
		if (message instanceof GameObject) {
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
		
		Class<?> store = Class.forName(Config.objectStore);
		ActorRef storeRef = this.getContext().actorOf(Props.create(store).withRouter(new RoundRobinRouter(10)), store.getSimpleName());
		
		this.getContext()
				.system()
				.scheduler()
				.schedule(Duration.Zero(), Duration.create(this.getMinTimeBetweenWrites(), TimeUnit.MILLISECONDS), this.getSelf(),
						"tick", this.getContext().system().dispatcher(), null);
	}

	public Boolean writeGameObject(GameObject gameObject) {
		this.lastWrite = System.currentTimeMillis();
		return true;
	}

	public Boolean eligibleForWrite(GameObject gameObject) {
		
		Long lastUpdated = gameObjectUpdates.get(gameObject.getId());
		
		// No lastUpdated means was put in the queue on the first try and was never written
		if (lastUpdated ==  null) {
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

	public void setGameObject(GameObject gameObject) {
		gameObjects.put(gameObject.getId(), gameObject);
		gameObjectUpdates.put(gameObject.getId(), System.currentTimeMillis());
	}

	public Boolean checkQueue() {
		if (queue.size() == 0) {
			return false;
		}
		currentGameObject = queue.get(queue.size() - 1);
		if (!busy() && eligibleForWrite(currentGameObject)) {
			if (writeGameObject(currentGameObject)) {
				gameObjectUpdates.put(currentGameObject.getId(), System.currentTimeMillis());
				queue.remove(queue.size() - 1);
				queueIndex.remove(currentGameObject.getId());
				return true;
			}
		}
		return false;
	}

	public Boolean write(Object message) {
		currentGameObject = (GameObject) message;
		Boolean writeThrough = true;

		if (gameObjects.containsKey(currentGameObject.getId())) {
			if (!busy() && eligibleForWrite(currentGameObject)) {
				writeThrough = true;
			} else {
				writeThrough = false;
			}
		} else if (busy()) {
			writeThrough = false;
		}

		gameObjects.put(currentGameObject.getId(), currentGameObject);
		if (writeThrough) {
			if (writeGameObject(currentGameObject)) {
				gameObjectUpdates.put(currentGameObject.getId(), System.currentTimeMillis());
				return writeThrough;
			}
		} else {
			// queue for later write
			if (!queueIndex.containsKey(currentGameObject.getId())) {
				queue.add(0, currentGameObject);
			}
		}
		return writeThrough;
	}
}
