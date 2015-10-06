package plugins;

import akka.actor.Props;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.combat.Combat;
import plugins.combat.CombatHandler;
import plugins.combat.Common;
import plugins.combat.PlayerSkillHandler;
import plugins.combat.StatusEffectHandler;

public class CombatPlugin extends Plugin {

	@Override
	public void start() {

		GameMessageRoute.add(PlayerSkillHandler.name, PlayerSkillHandler.name, false);
		ActorUtil.createActor(PlayerSkillHandler.class, PlayerSkillHandler.name);

		GameMessageRoute.add(Combat.name, Combat.name, false);
		ActorUtil.createActor(Combat.class, Combat.name);

		GameMessageRoute.add(CombatHandler.name, CombatHandler.name, false);
		ActorUtil.createActor(CombatHandler.class, CombatHandler.name);

		createEffectHandlers();
	}

	private void createEffectHandlers() {
		for (int i = 0; i < 3; i++) {
			GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "default" + i);
			GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "aoe" + i);
			GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "build_objects" + i);

			GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class, "default", i),
					StatusEffectHandler.actorName("default", i));
			GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class, "aoe", i),
					StatusEffectHandler.actorName("aoe", i));
			GameMachineLoader.getActorSystem().actorOf(Props.create(StatusEffectHandler.class, "build_objects", i),
					StatusEffectHandler.actorName("build_objects", i));

		}
	}
}