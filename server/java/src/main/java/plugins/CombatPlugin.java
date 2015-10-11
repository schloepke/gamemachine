package plugins;

import java.util.ArrayList;
import java.util.List;

import akka.actor.Props;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.combat.CombatHandler;
import plugins.combat.GridSet;
import plugins.combat.NpcDemo;
import plugins.combat.PlayerSkillHandler;
import plugins.combat.SimpleGameEntityManager;
import plugins.combat.StatusEffectDef;
import plugins.combat.StatusEffectHandler;
import plugins.combat.VitalsHandler;
import plugins.combat.VitalsSender;

public class CombatPlugin extends Plugin {

	public static List<GridSet> gridsets = new ArrayList<GridSet>();
	
	@Override
	public void start() {
		StatusEffectDef.createStatusEffects();
		
		SimpleGameEntityManager characterManager = new SimpleGameEntityManager();
		GameEntityManagerService.instance().setGameEntityManager(characterManager);
				
		GameMessageRoute.add(VitalsHandler.name, VitalsHandler.name, false);
		ActorUtil.createActor(VitalsHandler.class, VitalsHandler.name);
		
		GameMessageRoute.add(PlayerSkillHandler.name, PlayerSkillHandler.name, false);
		ActorUtil.createActor(PlayerSkillHandler.class, PlayerSkillHandler.name);

		GameMessageRoute.add(CombatHandler.name, CombatHandler.name, false);
		ActorUtil.createActor(CombatHandler.class, CombatHandler.name);

		createEffectHandlers();
		
		GameMachineLoader.getActorSystem().actorOf(Props.create(VitalsSender.class), VitalsSender.class.getSimpleName());
		
		NpcDemo.createDemoNpcs();
	}

	private void createEffectHandlers() {
		for (int i = 0; i < 3; i++) {
			GridSet gridSet = new GridSet();
			gridSet.zone = 0;
			
			gridSet.playerGrid = GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "default" + i);
			
			gridSet.objectGrid = GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "build_objects" + i);
			
			gridsets.add(gridSet);
			
			GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class, "default", i),
					StatusEffectHandler.actorName("default", i));
			GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class, "build_objects", i),
					StatusEffectHandler.actorName("build_objects", i));

		}
	}
}