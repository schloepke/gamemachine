package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.combat.CombatHandler;
import plugins.combat.PlayerSkillHandler;
import plugins.combat.StatusEffectHandler;
import plugins.pvp_game.CharacterHandler;
import plugins.pvp_game.ConsumableItemHandler;
import plugins.pvp_game.CraftingHandler;
import plugins.pvp_game.GridExpiration;
import plugins.pvp_game.GuildHandler;
import plugins.pvp_game.HarvestHandler;
import plugins.pvp_game.PlayerItemManager;
import plugins.pvp_game.SiegeHandler;
import plugins.pvp_game.TerritoryHandler;
import plugins.pvp_game.TimeHandler;

public class PvpGamePlugin extends Plugin {

	@Override
	public void start() {
		
		GameMessageRoute.add(CharacterHandler.name,CharacterHandler.name,false);
		GameMessageRoute.add(HarvestHandler.name,HarvestHandler.name,false);
		GameMessageRoute.add(CraftingHandler.name,CraftingHandler.name,false);
		
		
		GameMessageRoute.add(ConsumableItemHandler.name,ConsumableItemHandler.name,false);
		GameMessageRoute.add(SiegeHandler.name,SiegeHandler.name,false);
		GameMessageRoute.add(GuildHandler.name,GuildHandler.name,false);
		GameMessageRoute.add(TerritoryHandler.name,TerritoryHandler.name,false);
		
		ActorUtil.createActor(CharacterHandler.class, CharacterHandler.name);
		ActorUtil.createActor(HarvestHandler.class, HarvestHandler.name);
		ActorUtil.createActor(CraftingHandler.class, CraftingHandler.name);
		
		
		
		ActorUtil.createActor(ConsumableItemHandler.class, ConsumableItemHandler.name);
		ActorUtil.createActor(SiegeHandler.class, SiegeHandler.name);
		ActorUtil.createActor(GridExpiration.class, GridExpiration.name);
		ActorUtil.createActor(TimeHandler.class, TimeHandler.name);
		ActorUtil.createActor(GuildHandler.class, GuildHandler.name);
		ActorUtil.createActor(TerritoryHandler.class, TerritoryHandler.name);
		ActorUtil.createActor(ChatSubscriptions.class, ChatSubscriptions.name);

		

		PlayerItemManager.seedCharacterItem("wood", 50);
		PlayerItemManager.seedCharacterItem("iron_ore", 50);
	}

}
