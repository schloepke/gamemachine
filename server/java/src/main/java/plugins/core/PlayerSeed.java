
package plugins.core;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;

import java.util.List;

import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.typesafe.config.Config;

public class PlayerSeed extends GameMessageActor {

	public static String name = PlayerSeed.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private PlayerService ps;
	private CharacterService cs;
	private String gameId;
	
	@Override
	public void awake() {
		ps = PlayerService.getInstance();
		cs = CharacterService.getInstance();
		gameId = AppConfig.getDefaultGameId();
		
		Config config = Plugin.getConfig(PlayerSeed.class);
		List<String> playerDefs = config.getStringList("players");
		for (String value : playerDefs) {
			String[] parts = value.split(":");
			String id = parts[0];
			String charId = parts[1];
			String role = parts[2];
			String password = parts[3];
			
			Player player = ps.find(id);
			if (player == null) {
				player = ps.create(id, gameId,role);
				cs.create(id, charId, null);
			}

			ps.setPassword(id, password);
			ps.setCharacter(id, charId);
			ps.setRole(id, role);
			logger.debug("Player "+id+" seeded with character "+charId+" role "+role);
		}
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
	
		
	}

}
