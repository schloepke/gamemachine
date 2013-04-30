package com.game_machine.systems.memorydb;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.basho.riak.client.raw.pbc.PBClientConfig;

import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class RiakStore extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private IRiakClient client;
	private String bucketName = "testBucket";
	private Bucket bucket;
	private Integer writeInterval = 5000;
	private Integer maxWritesPerSecond = 50;
	private Integer minWriteInterval = 1000 / maxWritesPerSecond;
	private Long lastWrite = System.currentTimeMillis();
	public HashMap<String, GameObject> gameObjects = new HashMap<String, GameObject>();
	
	public RiakStore() {
		initRiak();
		Cancellable cancellable = this.getContext().system().scheduler()
				.schedule(Duration.Zero(), Duration.create(minWriteInterval, TimeUnit.MILLISECONDS), this.getSelf(), "tick", this.getContext().system().dispatcher(), null);
	}

	public void initRiak() {
		PBClientConfig conf = new PBClientConfig.Builder().withHost("192.168.130.128").withPort(8087).build();
		try {
			client = RiakFactory.newClient(conf);
		} catch (RiakException e) {
			e.printStackTrace();
		}
		
		try {
			bucket = client.createBucket(bucketName).execute();
			bucket = client.fetchBucket(bucketName).execute();
			bucket.store("key1", "value1").execute();
			IRiakObject myData = bucket.fetch("key1").execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeGameObject(GameObject gameObject) {
		try {
			bucket.store("key1", gameObject).execute();
		} catch (RiakRetryFailedException | UnresolvedConflictException | ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onReceive(Object message) {
		if (message instanceof GameObject) {
			GameObject gameObject = (GameObject) message;
		} else if (message instanceof String) {
			if (message.equals("tick")) {
				log.info("TICK");
			}
		} else {
			unhandled(message);
		}
	}
}
