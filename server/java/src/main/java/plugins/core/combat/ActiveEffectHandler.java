package plugins.core.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import plugins.landrush.BuildObjectHandler;
import scala.concurrent.duration.Duration;

public class ActiveEffectHandler extends UntypedActor {

	private Map<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();

	private AtomicLong counter = new AtomicLong();
	public static String name = ActiveEffectHandler.class.getSimpleName();
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private int zone = -1;
	private Grid grid = null;
	private String gridName = null;

	public static String actorName(String gridName, int zone) {
		return ActiveEffectHandler.name + gridName + zone;
	}

	public ActiveEffectHandler(String gridName, int zone) {
		this.gridName = gridName;
		this.zone = zone;
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), gridName, zone);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget statusEffectTarget = (StatusEffectTarget) message;
			useSkillOnTarget(statusEffectTarget);
		} else if (message instanceof String) {
			if (message.equals("effects_tick")) {
				for (StatusEffectTarget statusEffectTarget : targets.values()) {
					if ((System.currentTimeMillis() - statusEffectTarget.lastTick) >= 1000l) {
						applyEffects(statusEffectTarget);

						if (hasActiveEffect(statusEffectTarget)) {
							statusEffectTarget.lastTick = System.currentTimeMillis();
						} else {
							targets.remove(statusEffectTarget.activeId);
						}
					}
				}
				tick(300L, "effects_tick");
			}
		}
	}

	private void skillUp(PlayerSkill playerSkill, String characterId) {
		int chance = randInt(1, 10);
		if (chance < 5) {
			return;
		}

		Character character = CharacterService.instance().find(characterId);
		if (character == null) {
			logger.warning("Unable to find character for " + characterId);
			return;
		}

		if (PlayerSkillHandler.hasPlayerSkill(playerSkill.id, character.id)) {
			PlayerSkill existing = PlayerSkillHandler.playerSkill(playerSkill.id, character.id);
			existing.level += 1;
			PlayerSkillHandler.savePlayerSkill(existing);
			logger.warning("Skill up " + playerSkill.id + " level " + existing.level);
		}
	}

	private long useSkillOnTarget(StatusEffectTarget statusEffectTarget) {
		long activeId = setStatusEffects(statusEffectTarget);
		applyEffects(statusEffectTarget);
		statusEffectTarget.lastTick = System.currentTimeMillis();

		skillUp(statusEffectTarget.attack.playerSkill, statusEffectTarget.originCharacterId);
		return activeId;
	}

	private void convertToSingleTarget(StatusEffectTarget statusEffectTarget, String targetEntityId) {
		StatusEffectTarget newTarget = statusEffectTarget.clone();
		newTarget.getStatusEffectList().clear();
		newTarget.activeId = 0L;
		newTarget.lastTick = 0L;
		newTarget.targetEntityId = targetEntityId;
		newTarget.attack.playerSkill.category = PlayerSkill.Category.SingleTarget;
		targets.remove(statusEffectTarget.activeId);
		useSkillOnTarget(newTarget);
	}

	private boolean hasActiveEffect(StatusEffectTarget statusEffectTarget) {
		for (StatusEffect effect : statusEffectTarget.getStatusEffectList()) {
			if (effect.ticksPerformed < effect.ticks) {
				return true;
			}
		}
		return false;
	}

	private boolean effectLimitReached(String entityId, String effectId, int maxStacks) {
		List<StatusEffect> statusEffects = getStatusEffects(entityId);
		int count = 0;
		for (StatusEffect effect : statusEffects) {
			if (effect.id.equals(effectId)) {
				count++;
				if (count > maxStacks) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasEffect(String entityId, String effectId) {
		List<StatusEffect> statusEffects = getStatusEffects(entityId);
		for (StatusEffect effect : statusEffects) {
			if (effect.id.equals(effectId) && effect.ticksPerformed < effect.ticks) {
				return true;
			}
		}
		return false;
	}
	
	private int effectCount(String targetEntityId, String originEntityId, String effectId) {
		int count = 0;
		for (StatusEffectTarget target : targets.values()) {
			
			if (target.targetEntityId.equals(targetEntityId) && target.originEntityId.equals(originEntityId)) {
				for (StatusEffect effect : target.getStatusEffectList()) {
					if (effect.id.equals(effectId) && effect.ticksPerformed < effect.ticks) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	private List<StatusEffect> getStatusEffects(String entityId) {
		List<StatusEffect> statusEffects = new ArrayList<StatusEffect>();
		for (StatusEffectTarget target : targets.values()) {
			if (target.targetEntityId.equals(entityId)) {
				for (StatusEffect effect : target.getStatusEffectList()) {
					statusEffects.add(effect);
				}
			}
		}
		return statusEffects;
	}

	

	private long setStatusEffects(StatusEffectTarget statusEffectTarget) {
		boolean multi = false;
		for (StatusEffect effect : StatusEffectData.skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
			statusEffectTarget.addStatusEffect(effect.clone());
			if (effect.ticks > 1) {
				multi = true;
			}
		}

		if (multi) {
			statusEffectTarget.activeId = counter.getAndIncrement();
			targets.put(statusEffectTarget.activeId, statusEffectTarget);
			return statusEffectTarget.activeId;
		} else {
			return 0L;
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
			if (statusEffect.ticksPerformed < statusEffect.ticks) {
				// logger.warning("Tick " + statusEffect.ticksPerformed + " " +
				// statusEffect.id);

				if (!StatusEffectManager.DeductCost(originProxy, statusEffect)) {
					statusEffect.ticksPerformed += 1;
					continue;
				}

				if (statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.Aoe
						|| statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.AoeDot
						|| statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.Pbaoe) {

					for (TrackData trackData : AoeUtil.getTargetsInRange(statusEffect.range,
							statusEffectTarget.location, grid)) {

						if (statusEffectTarget.attack.playerSkill.category == PlayerSkill.Category.AoeDot) {
							VitalsProxy targetProxy = VitalsHandler.fromTrackData(trackData, zone);
							convertToSingleTarget(statusEffectTarget, targetProxy.vitals.entityId);
							continue;
						} else {
							VitalsProxy targetProxy = VitalsHandler.fromTrackData(trackData, zone);
							applyEffect(statusEffectTarget.attack.playerSkill, originProxy, targetProxy, statusEffect);
						}

					}
				} else {
					VitalsProxy targetProxy = VitalsHandler.get(statusEffectTarget.targetEntityId);
					applyEffect(statusEffectTarget.attack.playerSkill, originProxy, targetProxy, statusEffect);
				}
				statusEffect.ticksPerformed += 1;
			}
		}

	}

	private int applyEffect(PlayerSkill playerSkill, VitalsProxy originProxy, VitalsProxy targetProxy,
			StatusEffect statusEffect) {

		if (targetProxy.vitals.dead == 1) {
			return 0;
		}

		int effectCountFromOrigin = effectCount(targetProxy.vitals.entityId,originProxy.vitals.entityId,statusEffect.id);
		if (effectCountFromOrigin >= 2) {
			//logger.warning("Effect from same origin present");
			return 0;
		} else if (effectLimitReached(targetProxy.vitals.entityId, statusEffect.id, playerSkill.maxStacks)) {
			//logger.warning("Effect limit reached");
			return 0;
		}
		
		targetProxy.vitals.lastCombat = System.currentTimeMillis();

		int value = getEffectValue(statusEffect, playerSkill, originProxy.vitals.characterId);

		if (statusEffect.type == StatusEffect.Type.AttributeDecrease) {

			if (targetProxy.vitals.type == Vitals.VitalsType.Character) {
				// no damage to self
				if (targetProxy.vitals.characterId.equals(originProxy.vitals.characterId)) {
					return 0;
				}

				// or group members
				if (inSameGroup(originProxy.vitals.characterId, targetProxy.vitals.characterId)) {
					return 0;
				}
			}

			targetProxy.subtract(statusEffect.attribute, value);
		} else if (statusEffect.type == StatusEffect.Type.AttributeIncrease) {

			if (targetProxy.vitals.type == Vitals.VitalsType.Character) {
				// only to self or group members
				if (targetProxy.vitals.characterId.equals(originProxy.vitals.characterId)
						|| inSameGroup(originProxy.vitals.characterId, targetProxy.vitals.characterId)) {
					targetProxy.add("health", value);
				} else {
					return 0;
				}
			}
		}

		if (value > 0) {
			if (targetProxy.vitals.type == Vitals.VitalsType.BuildObject && statusEffect.attribute.equals("health")) {
				BuildObjectHandler.setHealth(targetProxy.vitals.entityId, targetProxy.vitals.health);
			}

			targetProxy.vitals.changed = 1;
			//logger.warning("Category " + playerSkill.category.toString() + " target " + targetProxy.vitals.entityId
			//		+ " damage " + value + " type " + statusEffect.type + " health " + targetProxy.vitals.health);
		}

		return value;
	}

	private boolean inGroup(String playerId) {
		String playerGroup = ChatSubscriptions.playerGroup(playerId);
		if (playerGroup.equals("nogroup")) {
			return false;
		} else {
			return true;
		}
	}

	private String playerGroup(String playerId) {
		return ChatSubscriptions.playerGroup(playerId);
	}

	private boolean inSameGroup(String playerId, String otherId) {
		if (!inGroup(playerId)) {
			return false;
		}

		String otherGroup = ChatSubscriptions.playerGroup(otherId);

		if (playerGroup(playerId).equals(otherGroup)) {
			return true;
		} else {
			return false;
		}
	}

	private int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		int base = randInt(statusEffect.minValue, statusEffect.maxValue);
		Character character = CharacterService.instance().find(characterId);
		int level = skillLevel(playerSkill.id, character.id);
		return base + level;
	}

	// Can be a skill or player item
	private int skillLevel(String id, String characterId) {
		if (PlayerSkillHandler.hasPlayerSkill(id, characterId)) {
			PlayerSkill playerSkill = PlayerSkillHandler.playerSkill(id, characterId);
			return playerSkill.level;
		} else {
			return 0;
		}
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	@Override
	public void preStart() {
		tick(300L, "effects_tick");
	}

	public void tick(long delay, String message) {
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}

}
