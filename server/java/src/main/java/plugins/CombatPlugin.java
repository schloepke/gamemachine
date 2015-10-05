package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.combat.Combat;
import plugins.combat.CombatHandler;
import plugins.combat.PlayerSkillHandler;
import plugins.combat.StatusEffectHandler;

public class CombatPlugin extends Plugin {

	@Override
	public void start() {
		
		GameMessageRoute.add(PlayerSkillHandler.name,PlayerSkillHandler.name,false);
		ActorUtil.createActor(PlayerSkillHandler.class, PlayerSkillHandler.name);
	
		GameMessageRoute.add(Combat.name,Combat.name,false);
		ActorUtil.createActor(Combat.class,Combat.name);
		
		GameMessageRoute.add(CombatHandler.name,CombatHandler.name,false);
		ActorUtil.createActor(CombatHandler.class, CombatHandler.name);
		
		ActorUtil.createActor(StatusEffectHandler.class, StatusEffectHandler.name);
	}
}