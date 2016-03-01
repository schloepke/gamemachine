package io.gamemachine.authentication;

import io.gamemachine.messages.Player;

public class PublicAuthenticator implements PlayerAuthenticator {

    public PublicAuthenticator(Player player) {

    }

    @Override
    public void setPassword(String password) {
    }

    @Override
    public boolean authenticate(String password) {
        return true;
    }

}
