package io.gamemachine.objectdb;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import org.mapdb.HTreeMap;

public class MemoryStore implements Storable {

	private Map<String, byte[]> cache;

	public MemoryStore() {
		this.cache = MemoryMap.getInstance().getMap();
	}

	@Override
	public boolean delete(String id) {
		cache.remove(id);
		return true;
	}

	@Override
	public void connect() {
	}

	@Override
	public boolean setString(String id, String message) {
		byte[] b = message.getBytes(Charset.forName("UTF-8"));
		cache.put(id, b);
		return true;
	}

	@Override
	public boolean setBytes(String id, byte[] message) {
		cache.put(id, message);
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
		// TODO Auto-generated method stub
		
	}

}
