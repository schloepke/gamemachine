
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

public class PlayerSeed extends GameMessageActor {

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
		Player[] players = gson.fromJson(json, Player[].class);
		
		for (Player template : players) {
			Player player = ps.find(template.id);
			if (player == null) {
				player = ps.create(template.id, gameId,template.role);
				cs.create(template.id, template.characterId, null);
			}

			ps.setPassword(player.id, template.passwordHash);
			ps.setCharacter(player.id, template.characterId);
			ps.setRole(player.id, template.role);
			logger.debug("Player "+template.id+" seeded with character "+template.characterId+" role "+template.role);
		}
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
	
		
	}

}
