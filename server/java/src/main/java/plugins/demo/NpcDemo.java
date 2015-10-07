package plugins.demo;

import com.typesafe.config.Config;

import io.gamemachine.messages.Character;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
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
		cs = CharacterService.instance();
		gameId = AppConfig.getDefaultGameId();
		createNpcs(npcCount,0,"z0");
		//createNpcs(npcCount*3,1,"z1");
	}

	private void createNpcs(int count,int zone, String prefix) {
		for (int i = 1; i < count; i++) {
			String playerId = prefix+"demonpc" + i;
			String characterId = prefix+"demochr" + i;
			Character character = null;

			Player player = ps.find(playerId);
			if (player == null) {
				player = ps.create(playerId, gameId);
				character = cs.create(playerId, characterId, null);
			}

			
			ps.setCharacter(playerId, characterId);
			GameGrid.setPlayerZone(playerId, zone);
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
