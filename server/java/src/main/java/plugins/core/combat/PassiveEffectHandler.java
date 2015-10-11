package plugins.core.combat;

import akka.actor.UntypedActor;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;

public class PassiveEffectHandler extends UntypedActor {

	public static String name = PassiveEffectHandler.class.getSimpleName();
	
	private int zone = -1;
	private Grid grid = null;

	public static String actorName(String gridName, int zone) {
		return PassiveEffectHandler.name + gridName + zone;
	}

	public PassiveEffectHandler(String gridName, int zone) {
		this.zone = zone;
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), gridName, zone);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	// private void setPassiveEffect(StatusEffectTarget statusEffectTarget,
		// Vitals targetVitals,
		// StatusEffect statusEffect) {
		// int value = statusEffect.minValue;
		// //setPassiveEffect(statusEffect.type, targetVitals, value, false);
		//
		// if (statusEffectTarget.action == StatusEffectTarget.Action.Apply
		// && statusEffectTarget.passiveFlag ==
		// StatusEffectTarget.PassiveFlag.AutoRemove) {
		// statusEffectTarget = statusEffectTarget.clone();
		// statusEffectTarget.targetEntityId = targetVitals.entityId;
		// statusEffect = statusEffect.clone();
		// statusEffectTarget.statusEffect.clear();
		// statusEffectTarget.action = StatusEffectTarget.Action.Remove;
		// // statusEffectTarget.passiveFlag =
		// // StatusEffectTarget.PassiveFlag.NA;
		// statusEffectTarget.addStatusEffect(statusEffect);
		// logger.warning("scheduling removal of " + statusEffect.type + " from " +
		// targetVitals.entityId + " in "
		// + statusEffect.ticks);
		// getContext().system().scheduler().scheduleOnce(Duration.create((long)
		// statusEffect.ticks, TimeUnit.SECONDS),
		// getSelf(), statusEffectTarget, getContext().dispatcher(), null);
		// }
		// }

}
