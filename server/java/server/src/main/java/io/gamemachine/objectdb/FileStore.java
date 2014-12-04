package io.gamemachine.objectdb;

import io.gamemachine.config.AppConfig;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class FileStore implements Storable {

	private Map<String, byte[]> cache;
	private DB db;

	public FileStore() {
	}

	@Override
	public boolean delete(String id) {
		cache.remove(id);
		return true;
	}

	@Override
	public void connect() {
		db = DBMaker.newFileDB(new File(AppConfig.Datastore.getMapdbPath())).closeOnJvmShutdown().make();
		this.cache = db.createHashMap("fcache").expireStoreSize(1).counterEnable().keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.BYTE_ARRAY).make();
	}

	@Override
	public boolean setString(String id, String message) {
		byte[] b = message.getBytes(Charset.forName("UTF-8"));
		cache.put(id, b);
		db.commit();
		return true;
	}

	@Override
	public boolean setBytes(String id, byte[] message) {
		cache.put(id, message);
		db.commit();
		return true;
	}

	@Override
	public String getString(String id) {
		if (cache.containsKey(id)) {
			try {
				return new String(cache.get(id), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage());
			}
		} else {
			return null;
		}
	}

	@Override
	public byte[] getBytes(String id) {
		if (cache.containsKey(id)) {
			return cache.get(id);
		} else {
			return null;
		}
	}

	@Override
	public void shutdown() {
		db.close();
		
	}
}
