package com.game_machine.objectdb;

public interface Storable {
	boolean setString(String id, String message);
	boolean setBytes(String id, byte[] message);
	boolean delete(String id);
	String getString(String id);
	byte[] getBytes(String id);
	void connect();
	void shutdown();
}
