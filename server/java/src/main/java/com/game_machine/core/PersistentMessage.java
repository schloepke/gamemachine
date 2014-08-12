package com.game_machine.core;

public interface PersistentMessage {
	void ormSaveAsync(String playerId);
	Boolean ormSave(String playerId);
	Boolean ormDelete(String playerId);
	String getPersistPlayerId();
	String getPersistAction();
}
