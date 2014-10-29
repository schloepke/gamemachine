package com.game_machine.objectdb;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;
import com.game_machine.core.AppConfig;

public class CouchbaseStore implements Storable {


	private CouchbaseClient client;
	private List<URI> nodes = new ArrayList<URI>();
	private long timeout = 100;
	
	public CouchbaseStore() {
	}

	public void shutdown() {
		client.shutdown();
	}
	
	@Override
	public boolean delete(String id) {
		client.delete(id);
		OperationFuture<Boolean> future = client.delete(id);
		try {
			future.get(timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void connect() {
		for (String url : AppConfig.Couchbase.getServers()) {
			nodes.add(URI.create(url));
		}
		try {
			client = new CouchbaseClient(nodes, AppConfig.Couchbase.getBucket(), AppConfig.Couchbase.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to connect to couchbase");
		}
	}

	@Override
	public boolean setString(String id, String value) {
		OperationFuture<Boolean> future =client.set(id, value);
		try {
			future.get(timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setBytes(String id, byte[] value) {
		OperationFuture<Boolean> future = client.set(id, value);
		try {
			future.get(timeout, TimeUnit.MILLISECONDS);
			return true;
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getString(String id) {
		Object value = null;
		value = client.get(id);
		if (value == null) {
			return null;
		} else {
			return (String) value;
		}
	}

	@Override
	public byte[] getBytes(String id) {
		Object value = null;
		value = client.get(id);
		if (value == null) {
			return null;
		} else {
			return (byte[]) value;
		}
	}

}
