
package plugins.core;

import com.google.gson.Gson;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Vitals;

public class PlayerSeed extends GameMessageActor {

    public static class PlayerJson {
        public String playerId;
        public String characterId;
        public String role;
        public String password;
    }

    public static String name = PlayerSeed.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private PlayerService ps;
    private CharacterService cs;
    private String gameId;


    @Override
    public void awake() {
        ps = PlayerService.getInstance();
        cs = CharacterService.instance();
        gameId = AppConfig.getDefaultGameId();

        Gson gson = new Gson();
        String json = Plugin.getJsonConfig(PlayerSeed.class);
        PlayerJson[] players = gson.fromJson(json, PlayerJson[].class);

        for (PlayerJson template : players) {
            Player.Role role = Player.Role.valueOf(template.role);
            Player player = ps.find(template.playerId);


            if (player == null) {
                player = ps.create(template.playerId, gameId, role);
                cs.create(template.playerId, template.characterId, Vitals.Template.PlayerTemplate, null);
            }

            ps.setPassword(player.id, template.password);
            ps.setCharacter(player.id, template.characterId);
            ps.setRole(player.id, role);

            logger.debug("Player " + template.playerId + " seeded with character " + template.characterId + " role " + template.role);
        }
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {


    }

}
