package plugins.core.combat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import akka.actor.UntypedActor;
import io.gamemachine.config.AppConfig;
import io.gamemachine.grid.GameGrid;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import scala.concurrent.duration.Duration;

public class PassiveEffectHandler extends UntypedActor {

	public class EffectInfo {
		public VitalsProxy vitalsProxy;
		public StatusEffect statusEffect;
		public PlayerSkill playerSkill;
		public int value;
		public long createdAt = 0L;

		public EffectInfo(int value, VitalsProxy vitalsProxy, StatusEffect statusEffect, PlayerSkill playerSkill) {
			this.value = value;
			this.vitalsProxy = vitalsProxy;
			this.statusEffect = statusEffect;
			this.playerSkill = playerSkill;
			createdAt = System.currentTimeMillis();
		}
	}

	public static String name = PassiveEffectHandler.class.getSimpleName();

	private Map<Long, EffectInfo> targets = new ConcurrentHashMap<Long, EffectInfo>();
	private int zone = -1;
	private Grid grid = null;
	private AtomicLong counter = new AtomicLong();

	public static String actorName(String gridName, int zone) {
		return PassiveEffectHandler.name + gridName + zone;
	}

	public PassiveEffectHandler(String gridName, int zone) {
		this.zone = zone;
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), gridName, zone);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget statusEffectTarget = (StatusEffectTarget) message;
			setStatusEffects(statusEffectTarget);
			applyEffects(statusEffectTarget);
		} else if (message instanceof String) {
			if (message.equals("effects_tick")) {
				expireEffects();
				tick(1000L, "effects_tick");
			}
		}
	}

	@Override
	public void preStart() {
		tick(1000L, "effects_tick");
	}

	public void tick(long delay, String message) {
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}

	private void setStatusEffects(StatusEffectTarget statusEffectTarget) {
		for (StatusEffect effect : StatusEffectData.skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
			if (effect.type == StatusEffect.Type.AttributeMaxDecrease || effect.type == StatusEffect.Type.AttributeMaxIncrease) {
				statusEffectTarget.addStatusEffect(effect.clone());
			}
			
		}
	}

	private void applyEffects(StatusEffectTarget statusEffectTarget) {
		VitalsProxy originProxy = VitalsHandler.get(statusEffectTarget.originEntityId);

		// attacker disconnected, removed themselves from the vitals pool
		if (originProxy == null) {
			targets.remove(statusEffectTarget.activeId);
			return;
		}

		originProxy.vitals.lastCombat = System.currentTimeMillis();

		for (StatusEffect statusEffect : statusEffectTarget.getStatusEffectList()) {
			if (!StatusEffectManager.DeductCost(originProxy, statusEffect)) {
				statusEffect.ticksPerformed += 1;
				continue;
			}

			if (statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.Aoe
					|| statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.AoeDot
					|| statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.Pbaoe) {

				for (TrackData trackData : AoeUtil.getTargetsInRange(statusEffect.range, statusEffectTarget.location,
						grid)) {
					VitalsProxy targetProxy = VitalsHandler.fromTrackData(trackData, zone);
					applyEffect(statusEffectTarget.attack.playerSkill, originProxy, targetProxy, statusEffect);
				}
			} else {
				VitalsProxy targetProxy = VitalsHandler.get(statusEffectTarget.targetEntityId);
				applyEffect(statusEffectTarget.attack.playerSkill, originProxy, targetProxy, statusEffect);
			}
		}
	}

	private void applyEffect(PlayerSkill playerSkill, VitalsProxy originProxy, VitalsProxy targetProxy,
			StatusEffect statusEffect) {

		if (targetProxy.vitals.dead == 1) {
			return;
		}

		if (hasEffect(targetProxy.vitals.entityId, statusEffect.id)) {
			return;
		}

		targetProxy.vitals.lastCombat = System.currentTimeMillis();
		int value = StatusEffectManager.getEffectValue(statusEffect, playerSkill, originProxy.vitals.characterId);

		if (statusEffect.type == StatusEffect.Type.AttributeMaxDecrease) {

			if (targetProxy.vitals.type == Vitals.VitalsType.Character) {
				// no damage to self
				if (targetProxy.vitals.characterId.equals(originProxy.vitals.characterId)) {
					return;
				}

				// or group members
				if (StatusEffectManager.inSameGroup(originProxy.vitals.characterId, targetProxy.vitals.characterId)) {
					return;
				}
			}

			targetProxy.subtractBase(statusEffect.attribute, value);
		} else if (statusEffect.type == StatusEffect.Type.AttributeMaxIncrease) {

			if (targetProxy.vitals.type == Vitals.VitalsType.Character) {
				// only to self or group members
				if (targetProxy.vitals.characterId.equals(originProxy.vitals.characterId) || StatusEffectManager
						.inSameGroup(originProxy.vitals.characterId, targetProxy.vitals.characterId)) {
					targetProxy.addBase(statusEffect.attribute, value);
				} else {
					return;
				}
			}
		}

		EffectInfo info = new EffectInfo(value, targetProxy, statusEffect, playerSkill);
		targets.put(counter.getAndIncrement(), info);
	}

	private void expireEffects() {
		for (long key : targets.keySet()) {
			EffectInfo info = targets.get(key);
			long time = info.statusEffect.ticks * 1000L;
			if (System.currentTimeMillis() - info.createdAt >= time) {
				if (info.statusEffect.type == StatusEffect.Type.AttributeMaxDecrease) {
					info.vitalsProxy.addBase(info.statusEffect.attribute, info.value);
				} else if (info.statusEffect.type == StatusEffect.Type.AttributeMaxIncrease) {
					info.vitalsProxy.subtractBase(info.statusEffect.attribute, info.value);
				}
				targets.remove(key);
			}
		}
	}

	private boolean hasEffect(String entityId, String effectId) {
		for (EffectInfo info : targets.values()) {
			if (info.vitalsProxy.vitals.entityId.equals(entityId) && info.statusEffect.id.equals(effectId)) {
				return true;
			}
		}
		return false;
	}

}
