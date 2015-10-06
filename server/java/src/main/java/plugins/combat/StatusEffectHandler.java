package plugins.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.DataRequest;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffect.Type;
import io.gamemachine.messages.StatusEffectResult;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.VisualEffect;
import io.gamemachine.messages.Vitals;
import plugins.inventoryservice.InventoryService;
import scala.concurrent.duration.Duration;

public class StatusEffectHandler extends UntypedActor {

	private static ConcurrentHashMap<String, Integer> vitalTicks = new ConcurrentHashMap<String, Integer>();
	private static ConcurrentHashMap<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();

	public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
	public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

	public static ConcurrentHashMap<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();

	public static AtomicLong counter = new AtomicLong();
	public static String name = StatusEffectHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private Grid grid = null;
	private PlayerVitalsHandler vitalsHandler = null;

	public static void tell(String gridName, int zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
		ActorSelection sel = ActorUtil.getSelectionByName(actorName(gridName, zone));
		sel.tell(statusEffectTarget, sender);
	}

	public static String actorName(String gridName, int zone) {
		return StatusEffectHandler.name + gridName + zone;
	}

	public StatusEffectHandler(String gridName, int zone) {
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), gridName, zone);
		vitalsHandler = PlayerVitalsHandler.getHandler(gridName, zone);
	}

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

	private void skillUp(String id, String characterId) {
		int chance = Common.randInt(1, 10);
		if (chance < 5) {
			return;
		}

		Character character = CharacterService.getInstance().find(characterId);
		if (character == null) {
			logger.warning("Unable to find character for " + characterId);
			return;
		}

		if (PlayerSkillHandler.hasPlayerSkill(id, character.id)) {
			PlayerSkill playerSkill = PlayerSkillHandler.playerSkill(id, character.id);
			playerSkill.level += 1;
			PlayerSkillHandler.savePlayerSkill(playerSkill);
			logger.warning("Skill up " + playerSkill.id + " level " + playerSkill.level);
		}
	}

	public static void setPassiveSkill(String skillId, String origin, String targetId, StatusEffectTarget.Action action,
			StatusEffectTarget.PassiveFlag passiveFlag) {
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
			Vitals vitals = vitalsHandler.findOrCreate(target.target);

			if (target.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
				StatusEffect effect = target.statusEffect.get(0);
				setPassiveEffect(effect.type, vitals, effect.minValue, true);
				logger.warning("Removing " + effect.type + " from " + vitals.id);
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
		Vitals vitals = vitalsHandler.findOrCreate(target);
		setPassiveEffect(statusEffect.type, vitals, value, false);
		sendStatusEffectResult(statusEffect.id, statusEffectTarget, target, statusEffect.ticks);

		if (statusEffectTarget.action == StatusEffectTarget.Action.Apply
				&& statusEffectTarget.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
			statusEffectTarget = statusEffectTarget.clone();
			statusEffectTarget.target = target;
			statusEffect = statusEffect.clone();
			statusEffectTarget.statusEffect.clear();
			statusEffectTarget.action = StatusEffectTarget.Action.Remove;
			// statusEffectTarget.passiveFlag =
			// StatusEffectTarget.PassiveFlag.NA;
			statusEffectTarget.addStatusEffect(statusEffect);
			logger.warning(
					"scheduling removal of " + statusEffect.type + " from " + vitals.id + " in " + statusEffect.ticks);
			getContext().system().scheduler().scheduleOnce(Duration.create((long) statusEffect.ticks, TimeUnit.SECONDS),
					getSelf(), statusEffectTarget, getContext().dispatcher(), null);
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
		if (effect.type != StatusEffect.Type.Speed && effect.type != StatusEffect.Type.AttributeIncrease
				&& effect.type != StatusEffect.Type.AttributeDecrease) {
			return true;
		} else {
			return false;
		}
	}

	private void applyStatusEffects(StatusEffectTarget statusEffectTarget) {
		List<StatusEffect> effectsToRemove = null;

		for (StatusEffect effect : statusEffectTarget.statusEffect) {
			if (effect.ticks > statusEffectTarget.ticks) {
				boolean aoe = false;
				GmVector3 location = null;

				if (statusEffectTarget.target.equals(PlayerSkill.DamageType.Aoe.toString())) {
					aoe = true;
					location = statusEffectTarget.location;
				}

				if (statusEffectTarget.target.equals(PlayerSkill.DamageType.SelfAoe.toString())) {
					aoe = true;
					location = new GmVector3();
					TrackData td = grid.get(statusEffectTarget.origin);
					location.xi = td.x;
					location.yi = td.y;
					location.zi = td.z;
				}

				Vitals originVitals = vitalsHandler.findOrCreate(statusEffectTarget.origin);
				originVitals.lastCombat = System.currentTimeMillis();

				if (!DeductCost(originVitals, statusEffectTarget.skill)) {
					return;
				}

				if (aoe) {

					if (statusEffectTarget.ticks == 0) {
						sendVisualEffect(effect, statusEffectTarget.location);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range, location, grid)) {
						Player player = PlayerService.getInstance().find(target);
						if (player != null) {
							String characterId = player.characterId;
							applyStatusEffect(statusEffectTarget, characterId, effect);
						} else {
							logger.warning("Player not found " + target);
						}

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

	private int applyStatusEffect(StatusEffectTarget statusEffectTarget, String target, StatusEffect effect) {
		if (isPassive(effect)) {
			setPassiveEffect(statusEffectTarget, target, effect);
			return 0;
		}

		String origin = statusEffectTarget.origin;

		Vitals vitals = vitalsHandler.findOrCreate(target);
		if (vitals.dead == 1) {
			return 0;
		}
		vitals.lastCombat = System.currentTimeMillis();

		Vitals targetTemplate = CharacterService.getInstance().getVitalsTemplate(vitals.id);

		int targetBaseHealth = targetTemplate.health;
		int value = getEffectValue(effect, statusEffectTarget.skill, origin);

		if (effect.type == StatusEffect.Type.AttributeDecrease) {

			// no damage to self
			if (vitals.id.equals(origin)) {
				return 0;
			}

			// or group members
			if (inSameGroup(origin, vitals.id)) {
				return 0;
			}

			vitals.health -= value;
			if (vitals.health < 0) {
				vitals.health = 0;
			}
		} else if (effect.type == StatusEffect.Type.AttributeIncrease) {

			// only to self or group members
			if (vitals.id.equals(origin) || inSameGroup(origin, vitals.id)) {
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
			logger.warning(
					"target " + vitals.id + " damage " + value + " type " + effect.type + " health " + vitals.health);
			sendStatusEffectResult(effect.id, statusEffectTarget, target, value);
		}

		return value;
	}

	private boolean DeductCost(Vitals vitals, String skillId) {
		PlayerSkill skill = PlayerSkillHandler.globalPlayerSkills.get(skillId);
		if (skill.resourceCostPerTick == 0) {
			return true;
		}

		if (skill.resource.equals(PlayerSkill.Resource.Stamina.toString())) {
			if (vitals.stamina < skill.resourceCost) {
				logger.warning("Insufficient stamina needed " + skill.resourceCost);
				return false;
			}
			vitals.stamina -= skill.resourceCost;
		} else if (skill.resource.equals(PlayerSkill.Resource.Magic.toString())) {
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

	private int getEffectValue(StatusEffect effect, String skillId, String characterId) {
		int base = Common.randInt(effect.minValue, effect.maxValue);
		Character character = CharacterService.getInstance().find(characterId);
		int level = skillLevel(skillId, character.id);
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

	private void die(Vitals vitals) {
		vitals.health = 0;
		vitals.stamina = 0;
		vitals.magic = 0;
		vitals.dead = 1;
		vitals.changed = 1;
	}

	private void revive(Vitals vitals, Character character) {
		Vitals template = CharacterService.getInstance().getVitalsTemplate(vitals.id);
		vitals.dead = 0;
		vitals.health = template.health;
		vitals.stamina = template.stamina;
		vitals.magic = template.magic;
		vitals.changed = 1;
	}

	private void updateVitals() {
		int regen;

		for (Vitals vitals : vitalsHandler.getVitals()) {
			if (vitals.type == Vitals.VitalsType.Guard) {
				regen = 1000;
			} else {
				if ((System.currentTimeMillis() - vitals.lastCombat) <= 5000l) {
					regen = 2;
				} else {
					regen = 20;
				}
			}

			Character character = CharacterService.getInstance().find(vitals.id);
			Vitals template = CharacterService.getInstance().getVitalsTemplate(vitals.id);
			int stamina = template.stamina;
			int magic = template.magic;
			int health = template.health;

			if (vitals.dead == 1) {
				if (deathTimer.containsKey(vitals.id)) {
					Long timeDead = deathTimer.get(vitals.id);
					Long timer = Common.deathTime;
					if ((System.currentTimeMillis() - timeDead) > timer) {
						revive(vitals, character);
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

			if (vitals.type == Vitals.VitalsType.Player) {
				TrackData trackData = grid.get(vitals.id);
				if (trackData != null) {
					if (trackData.hidden == 1) {
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

	private void sendVisualEffect(StatusEffect effect, GmVector3 location) {
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

	private void sendStatusEffectResult(String statusEffectId, StatusEffectTarget statusEffectTarget, String target,
			int value) {
		GameMessage msg = new GameMessage();
		StatusEffectResult result = new StatusEffectResult();
		result.origin = statusEffectTarget.origin;
		result.target = target;
		result.statusEffectId = statusEffectId;
		result.value = value;
		msg.statusEffectResult = result;

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
		for (Vitals vitals : vitalsHandler.getVitals()) {
			Vitals template = CharacterService.getInstance().getVitalsTemplate(vitals.id);
			Boolean send = false;
			if (vitals.changed == 1 || vitals.health < template.health || vitals.magic < template.magic
					|| vitals.stamina < template.stamina) {
				resetVitalTick(vitals.id);
				send = true;
				vitals.changed = 0;
			} else {
				int tick = nextVitalTick(vitals.id);
				if (tick < 4) {
					send = true;
				}
			}

			if (send) {
				GameMessage msg = new GameMessage();
				Vitals otherVitals = new Vitals();
				otherVitals.id = vitals.id;
				otherVitals.health = vitals.health;
				otherVitals.dead = vitals.dead;
				msg.vitals = otherVitals;

				for (TrackData trackData : grid.getAll()) {

					// Send full vitals to player, partial to other players
					if (trackData.id.equals(msg.vitals.playerId)) {
						GameMessage playerMsg = new GameMessage();
						playerMsg.vitals = vitalsHandler.findOrCreate(msg.vitals.id);
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
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}

}
