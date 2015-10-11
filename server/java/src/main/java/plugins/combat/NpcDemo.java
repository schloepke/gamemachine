package plugins.combat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import akka.actor.Props;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;

public class NpcDemo  {

	private static final Logger logger = LoggerFactory.getLogger(NpcDemo.class);
	private static PlayerService ps;
	private static CharacterService cs;
	private static int npcCount;
	
	public static void createDemoNpcs() {
		Config config = Plugin.getConfig(NpcDemo.class);
		npcCount = config.getInt("npcCount");
		
		ps = PlayerService.getInstance();
		cs = CharacterService.instance();
		createNpcs(npcCount,0,"z0");
		//createNpcs(npcCount*3,1,"z1");
	}

	private static void createNpcs(int count,int zone, String prefix) {
		for (int i = 1; i < count; i++) {
			String playerId = prefix+"demonpc" + i;
			String characterId = prefix+"demochr" + i;
			Character character = null;

			Player player = ps.find(playerId);
			if (player == null) {
				player = ps.create(playerId, AppConfig.getDefaultGameId());
				character = cs.create(playerId, characterId, null);
			}

			
			ps.setCharacter(playerId, characterId);
			GameGrid.setEntityZone(playerId, zone);
			GameMachineLoader.getActorSystem().actorOf(Props.create(NpcEntity.class, playerId, characterId));
		}
		logger.warn("NpcDemo started with "+npcCount+" npc's");
	}

}
