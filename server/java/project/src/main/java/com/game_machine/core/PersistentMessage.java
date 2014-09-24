package com.game_machine.core;

public interface PersistentMessage {
	void dbSaveAsync(String playerId);
	Boolean dbSave(String playerId);
	Boolean dbDelete(String playerId);
	String getPersistPlayerId();
	String getPersistAction();
}
