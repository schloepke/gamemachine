package pvp_game;

import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.util.Vector3;


public class Npc {

	public static ConcurrentHashMap<String, Npc> npcs = new ConcurrentHashMap<String, Npc>();
	
	public String name;
	public String characterId;
	public String characterBase;
	public Vector3 position = Vector3.zero();
	private Vector3 target = Vector3.zero();
	
	public static void CreateNpcs() {
		createNpc("npc_guard1","guard_male","Bob",new Vector3(-1144.03,-575.29,38.97));
		createNpc("npc_guard2","guard_male","Andy",new Vector3(-1150.59,-545.95,38.97));
		
		for (int x = 3; x < 100; x++) {
			createNpc("npc_guard"+x,"guard_male","Guard"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
		for (int x = 3; x < 50; x++) {
			createNpc("npc_bandit"+x,"bandit_male","Bandit"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
		for (int x = 50; x < 100; x++) {
			createNpc("npc_bandita"+x,"bandit_female","Bandita"+x,new Vector3(-1150.59,-545.95,38.97));
		}
		
	}
	
	public static Npc createNpc(String playerId, String baseCharacter, String characterId, Vector3 position) {
		Npc npc = new Npc();
		npc.name = playerId;
		npc.characterBase = baseCharacter;
		npc.characterId = characterId;
		npc.position = position;
		if (!PlayerService.getInstance().playerExists(npc.name)) {
			PlayerService.getInstance().create(npc.name, Common.gameId, "npc");
		}
		
		CharacterHandler.cloneCharacter("npc", baseCharacter, npc.name, npc.characterId);
		PlayerService.getInstance().setCharacter(npc.name, npc.characterId);
		npcs.put(npc.name, npc);
		return npc;
	}
}
