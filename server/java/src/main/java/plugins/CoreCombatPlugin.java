package plugins;

import akka.actor.Props;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.core.combat.ClientDbLoader;
import plugins.core.combat.CombatHandler;
import plugins.core.combat.PlayerSkillHandler;
import plugins.core.combat.SiegeHandler;
import plugins.core.combat.StatusEffectData;
import plugins.core.combat.StatusEffectManager;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsSender;

public class CoreCombatPlugin extends Plugin {

	
	
	@Override
	public void start() {
		StatusEffectData.createStatusEffects();
			
		GameMessageRoute.add(VitalsHandler.name, VitalsHandler.name, false);
		ActorUtil.createActor(VitalsHandler.class, VitalsHandler.name);
		
		GameMessageRoute.add(PlayerSkillHandler.name, PlayerSkillHandler.name, false);
		ActorUtil.createActor(PlayerSkillHandler.class, PlayerSkillHandler.name);

		GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectManager.class), StatusEffectManager.name);
		
		GameMessageRoute.add(CombatHandler.name, CombatHandler.name, false);
		ActorUtil.createActor(CombatHandler.class, CombatHandler.name);
		
		GameMachineLoader.getActorSystem().actorOf(Props.create(VitalsSender.class), VitalsSender.class.getSimpleName());
		
		GameMessageRoute.add(SiegeHandler.name,SiegeHandler.name,false);
		ActorUtil.createActor(SiegeHandler.class,SiegeHandler.name);
	}

	
}