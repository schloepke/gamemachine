package pvp_game;

import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameMachineLoader;
import akka.actor.Props;

public class GameLoader {

	public static void load() {
		
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
		GameMachineLoader.getActorSystem().actorOf(Props.create(ChatSubscriptions.class), ChatSubscriptions.name);
		
		Npc.CreateNpcs();
		
		PlayerItemManager.seedCharacterItem("wood", 50);
		PlayerItemManager.seedCharacterItem("iron_ore", 50);
	}
}
