package pvp_game;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.util.Vector3;

import java.io.File;

import pvp_game.npc.NpcDef;
import pvp_game.npc.NpcManager;
import pvp_game.pathfinding.Graph;
import pvp_game.pathfinding.GridImport;
import pvp_game.pathfinding.Node;
import pvp_game.pathfinding.PathResult;
import akka.actor.Props;

public class GameLoader {

	public static void load() {
		
//		File path = new File(AppConfig.getEnvRoot()+"/grid_data.bin");
//		GridImport gridImport = new GridImport();
//		Graph.graph = gridImport.importFromFile(path);
//		Node node;
//		Vector3 start = new Vector3(-813l, 1178l,0l);
//		Vector3 end = new Vector3(-787l,1175l,0l);
//		PathResult result = Graph.graph.findPath(start, end,false);
//		if (result.result) {
//			System.out.println("pathcount "+result.resultPath.getCount());
//			result.resultPath.add(new Node((int)end.x,(int)end.y));
//			for (int i = 0; i < result.resultPath.getCount(); i++) {
//				node = (Node) result.resultPath.get(i);
//				System.out.println(node.x+" "+node.y);
//			}
//		} else {
//			System.out.println("Path error "+result.error);
//			System.out.println(result.metrics.visitedNodes);
//		}
		
		
		GameMachineLoader.getActorSystem().actorOf(Props.create(CharacterHandler.class), CharacterHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(HarvestHandler.class), HarvestHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(CraftingHandler.class), CraftingHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(CombatHandler.class), CombatHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(PlayerSkillHandler.class), PlayerSkillHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class), StatusEffectHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(ConsumableItemHandler.class), ConsumableItemHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(SiegeHandler.class), SiegeHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(GridExpiration.class), GridExpiration.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(TimeHandler.class), TimeHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(GuildHandler.class), GuildHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(TerritoryHandler.class), TerritoryHandler.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(ChatSubscriptions.class), ChatSubscriptions.name);
		GameMachineLoader.getActorSystem().actorOf(Props.create(WorldBuilderHandler.class), WorldBuilderHandler.name);
		
		NpcDef.createAll();
		
		PlayerItemManager.seedCharacterItem("wood", 50);
		PlayerItemManager.seedCharacterItem("iron_ore", 50);
		
		//GameMachineLoader.getActorSystem().actorOf(Props.create(NpcManager.class), NpcManager.name);
		
	}
}
