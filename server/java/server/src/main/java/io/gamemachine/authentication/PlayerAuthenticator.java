package io.gamemachine.authentication;

public interface PlayerAuthenticator {
    void setPassword(String password);

    boolean authenticate(String password);
}
