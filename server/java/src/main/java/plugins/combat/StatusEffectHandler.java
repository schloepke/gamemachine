package plugins.combat;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerVitalsHandler;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.DataRequest;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectResult;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.VisualEffect;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.WorldObject;
import plugins.pvp_game.CharacterHandler;
import plugins.pvp_game.Common;
import plugins.pvp_game.ConsumableItemHandler;
import plugins.pvp_game.PlayerItemManager;
import plugins.pvp_game.PlayerSkillHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import static io.gamemachine.messages.StatusEffect.Type;

public class StatusEffectHandler extends UntypedActor {

	private static ConcurrentHashMap<String, Integer> vitalTicks = new ConcurrentHashMap<String, Integer>();
	private static ConcurrentHashMap<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();

	public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
	public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

	public static ConcurrentHashMap<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();

	public static AtomicLong counter = new AtomicLong();
	public static String name = StatusEffectHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget target = (StatusEffectTarget) message;
			useSkillOnTarget(target);
			sendVitals();
		} else if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				updateVitals();
				tick(1000L, "vitals_tick");
			} else if (message.equals("effects_tick")) {
				for (StatusEffectTarget target : targets.values()) {
					if ((System.currentTimeMillis() - target.lastTick) >= 1000l) {
						// logger.warning("Tick " + target.skill);
						applyStatusEffects(target);

						if (hasActiveEffect(target)) {
							target.lastTick = System.currentTimeMillis();
						} else {
							targets.remove(target.activeId);
						}
					}
				}
				tick(300L, "effects_tick");
			}
		} else if (message instanceof DataRequest) {
			DataRequest request = (DataRequest) message;
			for (StatusEffect effect : statusEffects.values()) {
				GameMessage msg = new GameMessage();
				msg.statusEffect = effect;
				PlayerCommands.sendGameMessage(msg, request.requester);
			}

		}
	}

	private void skillUp(String id, String playerId) {
		int chance = Common.randInt(1, 10);
		if (chance < 5) {
			return;
		}

		Character character = CharacterHandler.currentCharacter(playerId);
		if (character == null) {
			logger.warning("Unable to find character for " + playerId);
			return;
		}

		if (PlayerSkillHandler.hasPlayerSkill(id, character.id)) {
			PlayerSkill playerSkill = PlayerSkillHandler.playerSkill(id, character.id);
			playerSkill.level += 1;
			PlayerSkillHandler.savePlayerSkill(playerSkill);
			logger.warning("Skill up " + playerSkill.id + " level " + playerSkill.level);
		}
	}

	public static void setPassiveSkill(String skillId, String origin, String targetId,
			StatusEffectTarget.Action action, StatusEffectTarget.PassiveFlag passiveFlag) {
		StatusEffectTarget target = new StatusEffectTarget();
		target.action = action;
		target.passiveFlag = passiveFlag;
		target.range = 0;
		target.skill = skillId;
		target.origin = origin;
		target.target = targetId;
		ActorSelection effectHandler = ActorUtil.getSelectionByName(StatusEffectHandler.name);
		effectHandler.tell(target, null);
	}

	private long useSkillOnTarget(StatusEffectTarget target) {
		long activeId;
		
		if (target.action == StatusEffectTarget.Action.Remove) {
			Vitals vitals = PlayerVitalsHandler.getOrCreate("default", target.target);
			
			if (target.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
				StatusEffect effect = target.statusEffect.get(0);
				setPassiveEffect(effect.type, vitals, effect.minValue, true);
				logger.warning("Removing "+effect.type+" from "+vitals.id);
			} else {
				for (StatusEffect effect : skillEffects.get(target.skill)) {
					setPassiveEffect(effect.type, vitals, effect.minValue, true);
				}
			}
			activeId = 0;
		} else {
			activeId = setStatusEffects(target);
			applyStatusEffects(target);
			target.lastTick = System.currentTimeMillis();
		}

		skillUp(target.skill, target.origin);
		return activeId;
	}

	private StatusEffect.Type reverseEffect(StatusEffect.Type type) {
		StatusEffect.Type reversed;

		switch (type) {
		case SpellResistIncrease:
			reversed = Type.SpellResistDecrease;
			break;
		case SpellResistDecrease:
			reversed = Type.SpellResistIncrease;
			break;
		case ElementalResistIncrease:
			reversed = Type.ElementalResistDecrease;
			break;
		case ElementalResistDecrease:
			reversed = Type.ElementalResistIncrease;
			break;
		case ArmorIncrease:
			reversed = Type.ArmorDecrease;
			break;
		case ArmorDecrease:
			reversed = Type.ArmorIncrease;
			break;
		case MagicRegenIncrease:
			reversed = Type.MagicRegenDecrease;
			break;
		case HealthRegenIncrease:
			reversed = Type.HealthRegenDecrease;
			break;
		case StaminaRegenIncrease:
			reversed = Type.StaminaRegenDecrease;
			break;
		case MagicRegenDecrease:
			reversed = Type.MagicRegenIncrease;
			break;
		case StaminaRegenDecrease:
			reversed = Type.StaminaRegenIncrease;
			break;
		case Root:
			reversed = Type.Root;
			break;
		default:
			reversed = null;
			break;
		}
		return reversed;
	}

	private void setPassiveEffect(StatusEffect.Type type, Vitals vitals, int value, boolean reverse) {

		if (reverse) {
			type = reverseEffect(type);
		}

		logger.warning("Passive effect " + type + " value " + value);
		vitals.changed = 1;

		switch (type) {
		case SpellResistIncrease:
			vitals.spellResist += value;
			break;
		case SpellResistDecrease:
			vitals.spellResist -= value;
			break;
		case ElementalResistIncrease:
			vitals.elementalResist += value;
			break;
		case ElementalResistDecrease:
			vitals.elementalResist -= value;
			break;
		case ArmorIncrease:
			vitals.armor += value;
			break;
		case ArmorDecrease:
			vitals.armor -= value;
			break;
		case SpellPenetrationIncrease:
			vitals.spellPenetration += value;
			break;
		case MagicRegenIncrease:
			vitals.magicRegen += value;
			break;
		case HealthRegenIncrease:
			vitals.healthRegen += value;
			break;
		case StaminaRegenIncrease:
			vitals.staminaRegen += value;
			break;
		case MagicRegenDecrease:
			vitals.magicRegen -= value;
			break;
		case StaminaRegenDecrease:
			vitals.staminaRegen -= value;
			break;
		default:
			break;
		}

	}

	private void setPassiveEffect(StatusEffectTarget statusEffectTarget, String target, StatusEffect statusEffect) {
		int value = statusEffect.minValue;
		Vitals vitals = PlayerVitalsHandler.getOrCreate("default", target);
		setPassiveEffect(statusEffect.type, vitals, value, false);
		sendStatusEffectResult(statusEffect.id, statusEffectTarget.origin, target, statusEffect.ticks);
		
		if (statusEffectTarget.action == StatusEffectTarget.Action.Apply
				&& statusEffectTarget.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
			statusEffectTarget = statusEffectTarget.clone();
			statusEffectTarget.target = target;
			statusEffect = statusEffect.clone();
			statusEffectTarget.statusEffect.clear();
			statusEffectTarget.action = StatusEffectTarget.Action.Remove;
			//statusEffectTarget.passiveFlag = StatusEffectTarget.PassiveFlag.NA;
			statusEffectTarget.addStatusEffect(statusEffect);
			logger.warning("scheduling removal of "+statusEffect.type+" from "+vitals.id+" in "+statusEffect.ticks);
			getContext()
					.system()
					.scheduler()
					.scheduleOnce(Duration.create((long) statusEffect.ticks, TimeUnit.SECONDS), getSelf(), statusEffectTarget,
							getContext().dispatcher(), null);
		}
	}

	private long setStatusEffects(StatusEffectTarget target) {
		boolean multi = false;
		for (StatusEffect effect : skillEffects.get(target.skill)) {
			target.addStatusEffect(effect);
			if (effect.ticks > 1) {
				multi = true;
			}
		}

		target.ticks = 0;

		if (multi) {
			target.activeId = counter.getAndIncrement();
			targets.put(target.activeId, target);
			return target.activeId;
		} else {
			return 0l;
		}
	}

	private boolean hasActiveEffect(StatusEffectTarget target) {
		for (StatusEffect effect : target.statusEffect) {
			if (effect.ticks > target.ticks) {
				return true;
			}
		}
		return false;
	}

	private boolean isPassive(StatusEffect effect) {
		if (effect.type != StatusEffect.Type.Speed && effect.type != StatusEffect.Type.AttributeIncrease && effect.type != StatusEffect.Type.AttributeDecrease) {
			return true;
		} else {
			return false;
		}
	}

	private void applyStatusEffects(StatusEffectTarget statusEffectTarget) {
		List<StatusEffect> effectsToRemove = null;
		Grid defaultGrid = GameGrid.getPlayerGrid("default", statusEffectTarget.origin);
		Grid worldObjectGrid = GameGrid.getPlayerGrid("world_objects", statusEffectTarget.origin);
		
		for (StatusEffect effect : statusEffectTarget.statusEffect) {
			if (effect.ticks > statusEffectTarget.ticks) {
				boolean aoe = false;
				GmVector3 location = null;
				
				if (statusEffectTarget.target.equals("aoe")) {
					aoe = true;
					location = statusEffectTarget.location;
				}
				
				if (statusEffectTarget.target.equals("self_aoe")) {
					aoe = true;
					location = new GmVector3();
					TrackData td = defaultGrid.get(statusEffectTarget.origin);
					location.xi = td.x;
					location.yi = td.y;
					location.zi = td.z;
				}
				
				if (aoe) {
					
					if (statusEffectTarget.ticks == 0) {
						sendVisualEffect(effect, statusEffectTarget.location,defaultGrid);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range,	location, defaultGrid)) {
						applyStatusEffect(statusEffectTarget, target, effect);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range, location, worldObjectGrid)) {
						applyStatusEffect(statusEffectTarget, target, effect);
					}
				} else {
					applyStatusEffect(statusEffectTarget, statusEffectTarget.target, effect);
				}
			}
			if (isPassive(effect)) {
				if (effectsToRemove == null) {
					effectsToRemove = new ArrayList<StatusEffect>();
				}
				effectsToRemove.add(effect);
			}
		}

		if (effectsToRemove != null) {
			for (StatusEffect effect : effectsToRemove) {
				statusEffectTarget.removeStatusEffectById(effect);
			}
		}

		statusEffectTarget.ticks++;
	}

	private int applyStatusEffectToWorldObject(StatusEffectTarget statusEffectTarget, String target, StatusEffect effect) {

		String origin = statusEffectTarget.origin;

		if (effect.type == StatusEffect.Type.AttributeDecrease) {
			WorldObject worldObject = ConsumableItemHandler.wobjects.get(target);
			int damage = getEffectValue(effect, statusEffectTarget.skill, origin);
			if (damage > 0) {
				damage = damage / 4;
			}
			worldObject.health -= damage;
			logger.warning("applyStatusEffectToWorldObject " + target + " damage " + damage);

			if (worldObject.health <= 0) {
				if (worldObject.hasPlayerItemId()) {
					// ConsumableItemHandler.remove(worldObject.id);
				}
			}

			WorldObject.db().saveAsync(worldObject);

			return damage;
		} else {
			return 0;
		}

	}

	private int applyStatusEffect(StatusEffectTarget statusEffectTarget, String target, StatusEffect effect) {

		if (isPassive(effect)) {
			setPassiveEffect(statusEffectTarget, target, effect);
			return 0;
		}

		if (ConsumableItemHandler.wobjects.containsKey(target)) {
			return applyStatusEffectToWorldObject(statusEffectTarget, target, effect);
		}

		String origin = statusEffectTarget.origin;

		Vitals vitals = PlayerVitalsHandler.getOrCreate("default", target);
		if (vitals.dead == 1) {
			return 0;
		}
		vitals.lastCombat = System.currentTimeMillis();

		Vitals originVitals = PlayerVitalsHandler.getOrCreate("default", origin);
		originVitals.lastCombat = System.currentTimeMillis();
		
		if (!DeductCost(originVitals,statusEffectTarget.skill)) {
			return 0;
		}

		int targetBaseHealth = CharacterHandler.currentHealth(vitals.id);
		int value = getEffectValue(effect, statusEffectTarget.skill, origin);
		
		if (effect.type == StatusEffect.Type.AttributeDecrease) {
			
			// no damage to self
			if (vitals.id.equals(origin)) {
				return 0;
			}
			
			// or group members
			if (inSameGroup(origin,vitals.id)) {
				return 0;
			}
			
			vitals.health -= value;
			if (vitals.health < 0) {
				vitals.health = 0;
			}
		} else if (effect.type == StatusEffect.Type.AttributeIncrease) {
			
			// only to self or group members
			if (vitals.id.equals(origin) || inSameGroup(origin,vitals.id)) {
				vitals.health += value;
				if (vitals.health > targetBaseHealth) {
					vitals.health = targetBaseHealth;
				}
			} else {
				return 0;
			}
		}

		if (value > 0) {
			vitals.changed = 1;
			logger.warning("target " + vitals.id + " damage " + value + " type " + effect.type + " health "
					+ vitals.health);
			sendStatusEffectResult(effect.id, origin, target, value);
		}

		return value;
	}

	private boolean DeductCost(Vitals vitals, String skillId) {
		PlayerSkill skill = PlayerSkillHandler.globalPlayerSkills.get(skillId);
		if (!skill.hasResourceCostPerTick() || skill.resourceCostPerTick == 0) {
			return true;
		}
		
		if (skill.resource.equals("stamina")) {
			if (vitals.stamina < skill.resourceCost) {
				logger.warning("Insufficient stamina needed " + skill.resourceCost);
				return false;
			}
			vitals.stamina -= skill.resourceCost;
		} else if (skill.resource.equals("magic")) {
			if (vitals.magic < skill.resourceCost) {
				logger.warning("Insufficient magic needed " + skill.resourceCost);
				return false;
			}
			vitals.magic -= skill.resourceCost;
		}
		return true;
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
	
	private int getEffectValue(StatusEffect effect, String skillId, String playerId) {
		int base = Common.randInt(effect.minValue, effect.maxValue);
		Character character = CharacterHandler.currentCharacter(playerId);
		int level = skillLevel(skillId, character.id);
		return base + level;
	}

	// Can be a skill or player item
	private int skillLevel(String id, String characterId) {
		if (PlayerSkillHandler.hasPlayerSkill(id, characterId)) {
			PlayerSkill playerSkill = PlayerSkillHandler.playerSkill(id, characterId);
			return playerSkill.level;
		} else if (PlayerItemManager.hasPlayerItem(id, characterId)) {
			PlayerItem playerItem = PlayerItemManager.playerItem(id, characterId);
			return playerItem.level;
		} else {
			return 0;
		}
	}

	private void die(Vitals vitals) {
		vitals.health = 0;
		vitals.stamina = 0;
		vitals.magic = 0;
		vitals.dead = 1;
		vitals.changed = 1;
	}

	private void revive(Vitals vitals, Character character) {
		vitals.dead = 0;
		vitals.health = character.health;
		vitals.stamina = character.stamina;
		vitals.magic = character.magic;
		vitals.changed = 1;
	}

	private void updateVitals() {
		int regen;
		
		for (Vitals vitals : PlayerVitalsHandler.getVitals()) {
			if (vitals.id.startsWith("npc_guard")) {
				regen = 1000;
			} else {
				if ((System.currentTimeMillis() - vitals.lastCombat) <= 5000l) {
					regen = 2;
				} else {
					regen = 20;
				}
			}

			Character character = CharacterHandler.currentCharacter(vitals.id);

			int stamina = character.stamina;
			int magic = character.magic;
			int health = character.health;

			if (vitals.dead == 1) {
				if (deathTimer.containsKey(vitals.id)) {
					Long timeDead = deathTimer.get(vitals.id);
					Long timer;
					if (vitals.id.startsWith("bandit") || vitals.id.startsWith("npc")) {
						timer = Common.npcdDeathTime;
					} else {
						timer = Common.deathTime;
					}
					if ((System.currentTimeMillis() - timeDead) > timer) {
						revive(vitals,character);
						deathTimer.remove(vitals.id);
					}
				}
				continue;
			}

			if (vitals.health <= 0) {
				die(vitals);
				deathTimer.put(vitals.id, System.currentTimeMillis());
				continue;
			}

			if (vitals.health < health) {
				vitals.health += regen;
				vitals.changed = 1;
				if (vitals.health > health) {
					vitals.health = health;
				}
			}

			if (vitals.stamina < stamina) {
				vitals.stamina += regen;
				vitals.changed = 1;
				if (vitals.stamina > stamina) {
					vitals.stamina = stamina;
				}
			}

			if (vitals.magic < magic) {
				vitals.magic += regen;
				vitals.changed = 1;
				if (vitals.magic > magic) {
					vitals.magic = magic;
				}
			}
			
			if (!vitals.id.startsWith("npc")) {
				Grid grid = GameGrid.getPlayerGrid("default", vitals.id);
				TrackData trackData = grid.get(vitals.id);
				if (trackData != null) {
					if (trackData.hasHidden()) {
						int drain = 4;
						if (vitals.stamina >= drain) {
							vitals.stamina -= drain;
						}
					}
				}
				
			}
			
			
		}
		sendVitals();
	}

	private void sendVisualEffect(StatusEffect effect, GmVector3 location, Grid grid) {
		GameMessage msg = new GameMessage();
		VisualEffect v = new VisualEffect();
		v.prefab = effect.particleEffect;
		v.duration = effect.ticks;
		v.startPosition = location;
		msg.setVisualEffect(v);
		for (TrackData trackData : grid.getAll()) {
			PlayerCommands.sendGameMessage(msg, trackData.id);
		}
	}

	private void sendStatusEffectResult(String statusEffectId, String origin, String target, int value) {
		GameMessage msg = new GameMessage();
		StatusEffectResult result = new StatusEffectResult();
		result.origin = origin;
		result.target = target;
		result.statusEffectId = statusEffectId;
		result.value = value;
		msg.statusEffectResult = result;

		Grid grid = GameGrid.getGameGrid("mygame", "default",origin);
		for (TrackData trackData : grid.getAll()) {
			PlayerCommands.sendGameMessage(msg, trackData.id);
		}
	}

	private int nextVitalTick(String id) {
		if (vitalTicks.containsKey(id)) {
			int tick = vitalTicks.get(id);
			if (tick > 1000) {
				tick = 50;
			}
			vitalTicks.put(id, tick + 1);
			return tick;
		} else {
			vitalTicks.put(id, 0);
			return 0;
		}
	}

	private void resetVitalTick(String id) {
		vitalTicks.put(id, 0);
	}

	private void sendVitals() {
		Map<String, List<GameMessage>> gridMap = new HashMap<String, List<GameMessage>>();

		for (Vitals vitals : PlayerVitalsHandler.getVitals()) {
			int health = CharacterHandler.currentHealth(vitals.id);
			int stamina = CharacterHandler.currentStamina(vitals.id);
			int magic = CharacterHandler.currentMagic(vitals.id);
			Boolean send = false;
			if (vitals.changed == 1 || vitals.health < health || vitals.magic < magic || vitals.stamina < stamina) {
				resetVitalTick(vitals.id);
				send = true;
				vitals.changed = 0;
			} else {
				int tick = nextVitalTick(vitals.id);
				if (tick < 4) {
					send = true;
					// logger.warning("vitals tick " + vitals.id + " " + tick +
					// " health " + vitals.health);
				}
			}
			if (send) {
				GameMessage msg = new GameMessage();
				Vitals otherVitals = new Vitals();
				otherVitals.id = vitals.id;
				otherVitals.health = vitals.health;
				otherVitals.dead = vitals.dead;
				msg.vitals = otherVitals;
				
				if (!gridMap.containsKey(vitals.grid)) {
					gridMap.put(vitals.grid, new ArrayList<GameMessage>());
				}
				gridMap.get(vitals.grid).add(msg);
			}
		}

		for (String gridName : gridMap.keySet()) {
			Grid grid = GameGrid.xgetGameGrid(gridName);
			for (TrackData trackData : grid.getAll()) {
				for (GameMessage msg : gridMap.get(gridName)) {

					// Send full vitals to player, partial to other players
					if (trackData.id.equals(msg.vitals.id)) {
						GameMessage playerMsg = new GameMessage();
						playerMsg.vitals = PlayerVitalsHandler.get(msg.vitals.id);
						PlayerCommands.sendGameMessage(playerMsg, trackData.id);
					} else {
						PlayerCommands.sendGameMessage(msg, trackData.id);
					}
				}
			}
		}
	}

	@Override
	public void preStart() {
		StatusEffectDef.createStatusEffects();
		tick(300L, "effects_tick");
		tick(1000L, "vitals_tick");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}

}
