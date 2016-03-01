package io.gamemachine.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Player;

public class PlayerAuthentication {

    private static class LazyHolder {
        private static final PlayerAuthentication INSTANCE = new PlayerAuthentication();
    }

    public static PlayerAuthentication getInstance() {
        return LazyHolder.INSTANCE;
    }

    private PlayerAuthentication() {

    }

    public String authenticationResponse(int authToken, String playerId) {
        Map<String, String> data = new HashMap<String, String>();

        String protocol = AppConfig.Client.getProtocol();

        data.put("username", playerId);
        data.put("authtoken", Integer.toString(authToken));
        data.put("protocol", protocol);

        if (protocol.equals("TCP")) {
            data.put("tcp_host", AppConfig.Client.getTcpHost());
            data.put("tcp_port", Integer.toString(AppConfig.Client.getTcpPort()));
        } else if (protocol.equals("UDP")) {
            data.put("udp_host", AppConfig.Client.getUdpHost());
            data.put("udp_port", Integer.toString(AppConfig.Client.getUdpPort()));
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(data);
        return json;
    }

    public int authorize(String playerId, String password) {
        Player player = PlayerService.getInstance().find(playerId);
        PlayerAuthenticator authenticator = getAuthenticator(player);
        if (authenticator.authenticate(password)) {
            Random rand = new Random();
            int authtoken = rand.nextInt((9999999) + 1) + 1;
            PlayerService.getInstance().setAuthtoken(player.id, authtoken);
            return authtoken;
        } else {
            return 0;
        }

    }

    public boolean isPublic() {
        return AppConfig.Handlers.getAuth().equals("io.gamemachine.authentication.PublicAuthenticator");
    }

    private PlayerAuthenticator getAuthenticator(Player player) {
        if (AppConfig.Handlers.getAuth().equals("io.gamemachine.authentication.DefaultAuthenticator")) {
            return new DefaultAuthenticator(player);
        } else if (AppConfig.Handlers.getAuth().equals("io.gamemachine.authentication.DbAuthenticator")) {
            return new DbAuthenticator(player);
        } else {
            return new PublicAuthenticator(player);
        }
    }

}
