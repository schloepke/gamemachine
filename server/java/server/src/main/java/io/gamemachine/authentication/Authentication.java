package io.gamemachine.authentication;

import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Player;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class Authentication {

    private static ConcurrentHashMap<String, Integer> authenticatedUsers = new ConcurrentHashMap<String, Integer>();
    private static final Logger logger = LoggerFactory.getLogger(Authentication.class);


    public Authentication() {
    }

    public static boolean isAuthenticated(Player player) {
        return (authenticatedUsers.containsKey(player.id));
    }

    public static boolean isAuthenticated(String playerId) {
        return (authenticatedUsers.containsKey(playerId));
    }

    public static boolean hasValidAuthtoken(String playerId, int authtoken) {
        Player player = new Player();
        player.id = playerId;
        player.authtoken = authtoken;
        return Authentication.hasValidAuthtoken(player);
    }

    public static boolean hasValidAuthtoken(Player player) {
        if (authenticatedUsers.containsKey(player.getId())) {
            int authtoken = authenticatedUsers.get(player.id);
            if (authtoken == player.authtoken) {
                return true;
            } else {
                // Reload from database to see if it's been updated
                authtoken = PlayerService.getInstance().getAuthtoken(player.id);
                if (authtoken == player.authtoken) {
                    authenticatedUsers.put(player.id, authtoken);
                    return true;
                }
            }
        }
        return false;
    }

    public static void unregisterPlayer(String playerId) {
        authenticatedUsers.remove(playerId);
    }

    public void registerPlayer(Player player) {
        authenticatedUsers.put(player.getId(), authtokenForPlayer(player));
    }

    public Integer authtokenForPlayer(Player player) {
        if (isPublic()) {
            return player.getAuthtoken();
        } else {
            return PlayerService.getInstance().getAuthtoken(player.id);
        }
    }

    public boolean isPublic() {
        return PlayerAuthentication.getInstance().isPublic();
    }

    public boolean authtokenIsValid(Player player) {
        // 0 signifies logged out in the db, it's reserved and clients should never send this
        if (player.authtoken == 0) {
            return false;
        } else if (player.authtoken == authtokenForPlayer(player)) {
            return true;
        } else if (isPublic()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean authenticate(Player player) {
        if (Strings.isNullOrEmpty(player.id)) {
            return false;
        }
        if (authtokenIsValid(player)) {
            registerPlayer(player);
            player.setAuthenticated(true);
            return true;
        } else {
            logger.warn("Authentication for " + player.getId() + " failed");
            return false;
        }
    }

    public boolean ipCheck(String playerId) {
        Player player = PlayerService.getInstance().find(playerId);
        if (player.ipChangedAt == 0) {  // has not changed since player created
            return true;
        } else if ((System.currentTimeMillis() - player.ipChangedAt) < 60000) {
            return false;
        } else {
            return true;
        }
    }

}
