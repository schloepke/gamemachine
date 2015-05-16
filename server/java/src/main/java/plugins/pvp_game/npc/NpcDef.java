package plugins.pvp_game.npc;

import io.gamemachine.core.PlayerService;
import io.gamemachine.util.Vector3;

import java.util.concurrent.ConcurrentHashMap;

import plugins.pvp_game.CharacterHandler;
import plugins.pvp_game.Common;


public class NpcDef {

	public static ConcurrentHashMap<String, NpcDef> npcs = new ConcurrentHashMap<String, NpcDef>();
	
	public String name;
	public String characterId;
	public String characterBase;
	public Vector3 position = Vector3.zero();
	private Vector3 target = Vector3.zero();
	
	public static void createAll() {
		createOne("npc_guard1","guard_male","Bob",new Vector3(-1144.03,-575.29,38.97));
		createOne("npc_guard2","guard_male","Andy",new Vector3(-1150.59,-545.95,38.97));
		
		for (int x = 3; x < 100; x++) {
			createOne("npc_guard"+x,"guard_male","Guard"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
		for (int x = 3; x < 50; x++) {
			createOne("npc_bandit"+x,"bandit_male","Bandit"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
		for (int x = 50; x < 200; x++) {
			createOne("npc_bandita"+x,"bandit_female","Bandita"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
	}
	
	public static NpcDef createOne(String playerId, String baseCharacter, String characterId, Vector3 position) {
		NpcDef npc = new NpcDef();
		npc.name = playerId;
		npc.characterBase = baseCharacter;
		npc.characterId = characterId;
		npc.position = position;
		if (!PlayerService.getInstance().playerExists(npc.name,false)) {
			PlayerService.getInstance().create(npc.name, Common.gameId, "npc");
		}
		
		CharacterHandler.cloneCharacter("npc", baseCharacter, npc.name, npc.characterId);
		PlayerService.getInstance().setCharacter(npc.name, npc.characterId);
		npcs.put(npc.name, npc);
		return npc;
	}
}
