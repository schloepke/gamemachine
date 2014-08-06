package com.game_machine.core;

public interface PersistentMessage {
	void saveAsync(String playerId);
	Boolean save(String playerId);
	Boolean delete(String playerId);
	String getPersistPlayerId();
	String getPersistAction();
}
