package plugins.demo;

import com.typesafe.config.Config;

import io.gamemachine.messages.Character;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class NpcDemo extends GameMessageActor {

	public static String name = NpcDemo.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	private PlayerService ps;
	private CharacterService cs;
	private String gameId;
	private int npcCount;
	
	@Override
	public void awake() {
		Config config = Plugin.getConfig(NpcDemo.class);
		npcCount = config.getInt("npcCount");
		
		ps = PlayerService.getInstance();
		cs = CharacterService.getInstance();
		gameId = AppConfig.getDefaultGameId();
		createNpcs();
	}

	private void createNpcs() {
		for (int i = 1; i < npcCount; i++) {
			String playerId = "demonpc" + i;
			String characterId = "demochr" + i;
			Character character = null;

			Player player = ps.find(playerId);
			if (player == null) {
				player = ps.create(playerId, gameId);
				character = cs.create(playerId, characterId, null);
			}

			ps.setCharacter(playerId, characterId);
			Props props = Props.create(NpcEntity.class, playerId, characterId);
			ActorRef ref = getContext().actorOf(props, characterId);
		}
		logger.warning("NpcDemo started with "+npcCount+" npc's");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub

	}

}
