package com.game_machine.persistence;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.raw.pbc.PBClientConfig;

public class Riak {

	private static IRiakClient client;
	
	public static void init() {
		if (client instanceof IRiakClient) {
			return;
		}
		PBClientConfig conf = new PBClientConfig.Builder().withHost("192.168.130.128").withPort(8087).build();
		try {
			client = RiakFactory.newClient(conf);
		} catch (RiakException e) {
			e.printStackTrace();
		}
	}
	
	public static IRiakClient getClient() {
		return client;
	}
	
	public static void example() {
		Bucket bucket;
		String bucketName = "test";
		try {
			bucket = client.createBucket(bucketName).execute();
			bucket = client.fetchBucket(bucketName).execute();
			bucket.store("key1", "value1").execute();
			IRiakObject myData = bucket.fetch("key1").execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
