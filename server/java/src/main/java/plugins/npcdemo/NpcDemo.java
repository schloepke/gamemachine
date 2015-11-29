package plugins.npcdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import akka.actor.Props;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.PlayerService;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;

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
		createNpcs(npcCount,ZoneService.defaultZone(),"z0");
		//createNpcs(npcCount*3,1,"z1");
	}

	private static void createNpcs(int count,Zone zone, String prefix) {
		for (int i = 1; i < count; i++) {
			String playerId = prefix+"demonpc" + i;
			String characterId = prefix+"demochr" + i;
			Character character = cs.find(playerId, characterId);

			Player player = ps.find(playerId);
			if (player == null) {
				player = ps.create(playerId, AppConfig.getDefaultGameId());
			}
			
			if (character == null) {
				character = cs.create(playerId, characterId, null);
				character.gameEntityPrefab = "npc1";
				cs.save(character);
			}
			
			ps.setCharacter(playerId, characterId);
			PlayerService.getInstance().setZone(playerId, zone);
			GameMachineLoader.getActorSystem().actorOf(Props.create(NpcEntity.class, playerId, characterId));
		}
		logger.warn("NpcDemo started with "+npcCount+" npc's");
	}

}
