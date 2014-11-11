package io.gamemachine.authentication;


public interface Authable {
	String authtoken_for(String playerId);
	String authorize(String playerId, String password);
	boolean isPublic();
}
