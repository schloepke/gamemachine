package com.game_machine.authentication;

public interface PlayerAuthenticator {
	void setPassword(String password);
	boolean authenticate(String password);
}
