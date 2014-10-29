package com.game_machine.authentication;


public interface Authable {
	String authtoken_for(String playerId);
	String authorize(String playerId, String password);
	boolean isPublic();
}
