package io.gamemachine.authentication;


public interface Authable {
	String authtoken_for(String playerId);
	Integer authorize(String playerId, String password);
	boolean isPublic();
}
