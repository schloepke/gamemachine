package plugins.combat;

import java.util.ArrayList;
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
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.DataRequest;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffect.Type;
import io.gamemachine.messages.StatusEffectResult;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.VisualEffect;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import scala.concurrent.duration.Duration;

public class StatusEffectHandler extends UntypedActor {

	public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
	public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

	private Map<String, Integer> vitalTicks = new ConcurrentHashMap<String, Integer>();
	private Map<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();
	private Map<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();

	private AtomicLong counter = new AtomicLong();
	private static String name = StatusEffectHandler.class.getSimpleName();
	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private String gridName = null;
	private int zone = -1;
	private Grid grid = null;
	private VitalsHandler vitalsHandler = null;

	public static void tell(String gridName, int zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
		ActorSelection sel = ActorUtil.getSelectionByName(actorName(gridName, zone));
		sel.tell(statusEffectTarget, sender);
	}

	public static String actorName(String gridName, int zone) {
		return StatusEffectHandler.name + gridName + zone;
	}

	public StatusEffectHandler(String gridName, int zone) {
		this.gridName = gridName;
		this.zone = zone;
		grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), gridName, zone);
		vitalsHandler = VitalsHandler.getHandler(gridName, zone);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget statusEffectTarget = (StatusEffectTarget) message;
			useSkillOnTarget(statusEffectTarget);
			sendVitals();
		} else if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				updateVitals();
				tick(1000L, "vitals_tick");
			} else if (message.equals("effects_tick")) {
				for (StatusEffectTarget statusEffectTarget : targets.values()) {
					if ((System.currentTimeMillis() - statusEffectTarget.lastTick) >= 1000l) {
						applyStatusEffects(statusEffectTarget);

						if (hasActiveEffect(statusEffectTarget)) {
							statusEffectTarget.lastTick = System.currentTimeMillis();
						} else {
							targets.remove(statusEffectTarget.activeId);
						}
					}
				}
				tick(300L, "effects_tick");
			}
		} else if (message instanceof DataRequest) {
			DataRequest dataRequest = (DataRequest) message;
			for (StatusEffect effect : statusEffects.values()) {
				GameMessage msg = new GameMessage();
				msg.statusEffect = effect;
				PlayerCommands.sendGameMessage(msg, dataRequest.requester);
			}

		}
	}

	private void skillUp(PlayerSkill playerSkill, String characterId) {
		int chance = Common.randInt(1, 10);
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
		long activeId;

		if (statusEffectTarget.action == StatusEffectTarget.Action.Remove) {
			Vitals vitals = fromTarget(statusEffectTarget);

			if (statusEffectTarget.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
				StatusEffect effect = statusEffectTarget.statusEffect.get(0);
				setPassiveEffect(effect.type, vitals, effect.minValue, true);
				logger.warning("Removing " + effect.type + " from " + vitals.entityId);
			} else {
				for (StatusEffect effect : skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
					setPassiveEffect(effect.type, vitals, effect.minValue, true);
				}
			}
			activeId = 0;
		} else {
			activeId = setStatusEffects(statusEffectTarget);
			applyStatusEffects(statusEffectTarget);
			statusEffectTarget.lastTick = System.currentTimeMillis();
		}

		skillUp(statusEffectTarget.attack.playerSkill, statusEffectTarget.originCharacterId);
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

	private void setPassiveEffect(StatusEffectTarget statusEffectTarget, Vitals targetVitals,
			StatusEffect statusEffect) {
		int value = statusEffect.minValue;
		setPassiveEffect(statusEffect.type, targetVitals, value, false);
		sendStatusEffectResult(statusEffect.id, statusEffectTarget, targetVitals, statusEffect.ticks);

		if (statusEffectTarget.action == StatusEffectTarget.Action.Apply
				&& statusEffectTarget.passiveFlag == StatusEffectTarget.PassiveFlag.AutoRemove) {
			statusEffectTarget = statusEffectTarget.clone();
			statusEffectTarget.target = targetVitals.entityId;
			statusEffect = statusEffect.clone();
			statusEffectTarget.statusEffect.clear();
			statusEffectTarget.action = StatusEffectTarget.Action.Remove;
			// statusEffectTarget.passiveFlag =
			// StatusEffectTarget.PassiveFlag.NA;
			statusEffectTarget.addStatusEffect(statusEffect);
			logger.warning("scheduling removal of " + statusEffect.type + " from " + targetVitals.entityId + " in "
					+ statusEffect.ticks);
			getContext().system().scheduler().scheduleOnce(Duration.create((long) statusEffect.ticks, TimeUnit.SECONDS),
					getSelf(), statusEffectTarget, getContext().dispatcher(), null);
		}
	}

	private long setStatusEffects(StatusEffectTarget statusEffectTarget) {
		boolean multi = false;
		for (StatusEffect effect : skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
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
			return 0l;
		}
	}

	private boolean hasActiveEffect(StatusEffectTarget statusEffectTarget) {
		for (StatusEffect effect : statusEffectTarget.statusEffect) {
			if (effect.ticksPerformed < effect.ticks) {
				return true;
			}
		}
		return false;
	}

	private boolean isPassive(StatusEffect statusEffect) {
		if (statusEffect.type != StatusEffect.Type.Speed && statusEffect.type != StatusEffect.Type.AttributeIncrease
				&& statusEffect.type != StatusEffect.Type.AttributeDecrease) {
			return true;
		} else {
			return false;
		}
	}

	private Vitals fromTarget(StatusEffectTarget statusEffectTarget) {
		if (statusEffectTarget.attack.targetType == Attack.TargetType.Character) {
			return vitalsHandler.findOrCreateCharacterVitals(statusEffectTarget.targetEntityId);
		} else if (statusEffectTarget.attack.targetType == Attack.TargetType.Object) {
			return vitalsHandler.findOrCreateObjectVitals(statusEffectTarget.targetEntityId, Vitals.VitalsType.Object.number, zone);
		} else {
			throw new RuntimeException("Invalid target type " + statusEffectTarget.attack.targetType);
		}
	}

	private void applyStatusEffects(StatusEffectTarget statusEffectTarget) {
		List<StatusEffect> effectsToRemove = null;

		for (StatusEffect statusEffect : statusEffectTarget.statusEffect) {
			if (statusEffect.ticksPerformed < statusEffect.ticks) {
				logger.warning("Tick " + statusEffect.ticksPerformed + " " + statusEffect.id);
				boolean aoe = false;
				GmVector3 location = null;

				if (statusEffectTarget.attack.playerSkill.damageType.equals(PlayerSkill.DamageType.Aoe.toString())) {
					aoe = true;
					location = statusEffectTarget.location;
				} else if (statusEffectTarget.attack.playerSkill.damageType
						.equals(PlayerSkill.DamageType.Pbaoe.toString())) {
					aoe = true;
					location = new GmVector3();
					TrackData td = grid.get(statusEffectTarget.originEntityId);
					location.xi = td.x;
					location.yi = td.y;
					location.zi = td.z;
				}

				if (aoe) {
					if (statusEffect.ticksPerformed == 0) {
						sendVisualEffect(statusEffect, statusEffectTarget.location);
					}
					for (TrackData trackData : Common.getTargetsInRange(statusEffect.range, location, grid)) {
						Vitals vitals = vitalsHandler.fromTrackData(trackData, zone);
						applyStatusEffect(statusEffectTarget, vitals, statusEffect);
					}
				} else {
					Vitals vitals = fromTarget(statusEffectTarget);
					applyStatusEffect(statusEffectTarget, vitals, statusEffect);
				}
				statusEffect.ticksPerformed += 1;
			}
			if (isPassive(statusEffect)) {
				if (effectsToRemove == null) {
					effectsToRemove = new ArrayList<StatusEffect>();
				}
				effectsToRemove.add(statusEffect);
			}
		}

		if (effectsToRemove != null) {
			for (StatusEffect effect : effectsToRemove) {
				statusEffectTarget.removeStatusEffectById(effect);
			}
		}

	}

	private int applyStatusEffect(StatusEffectTarget statusEffectTarget, Vitals targetVitals,
			StatusEffect statusEffect) {
		if (isPassive(statusEffect)) {
			setPassiveEffect(statusEffectTarget, targetVitals, statusEffect);
			return 0;
		}

		if (targetVitals.dead == 1) {
			return 0;
		}

		Vitals originVitals = vitalsHandler.findOrCreateCharacterVitals(statusEffectTarget.originCharacterId);
		originVitals.lastCombat = System.currentTimeMillis();

		if (!DeductCost(originVitals, statusEffect)) {
			return 0;
		}

		targetVitals.lastCombat = System.currentTimeMillis();

		Vitals targetTemplate = VitalsHandler.getTemplate(targetVitals);

		int targetBaseHealth = targetTemplate.health;
		int value = getEffectValue(statusEffect, statusEffectTarget.attack.playerSkill,
				statusEffectTarget.originCharacterId);

		if (statusEffect.type == StatusEffect.Type.AttributeDecrease) {

			if (targetVitals.type == Vitals.VitalsType.Character) {
				// no damage to self
				if (targetVitals.characterId.equals(statusEffectTarget.originCharacterId)) {
					return 0;
				}

				// or group members
				if (inSameGroup(statusEffectTarget.originCharacterId, targetVitals.characterId)) {
					return 0;
				}
			}

			targetVitals.health -= value;
			if (targetVitals.health < 0) {
				targetVitals.health = 0;
			}
		} else if (statusEffect.type == StatusEffect.Type.AttributeIncrease) {

			if (targetVitals.type == Vitals.VitalsType.Character) {
				// only to self or group members
				if (targetVitals.characterId.equals(statusEffectTarget.originCharacterId)
						|| inSameGroup(statusEffectTarget.originCharacterId, targetVitals.characterId)) {
					targetVitals.health += value;
					if (targetVitals.health > targetBaseHealth) {
						targetVitals.health = targetBaseHealth;
					}
				} else {
					return 0;
				}
			}
		}

		if (value > 0) {
			targetVitals.changed = 1;
			// logger.warning("target " + targetVitals.id + " damage " + value +
			// " type " + effect.type + " health " + targetVitals.health);
			sendStatusEffectResult(statusEffect.id, statusEffectTarget, targetVitals, value);
		}

		return value;
	}

	private boolean DeductCost(Vitals vitals, StatusEffect statusEffect) {
		if (statusEffect.resourceCost == 0) {
			return true;
		}

		if (statusEffect.resource == StatusEffect.Resource.ResourceStamina) {
			if (vitals.stamina < statusEffect.resourceCost) {
				logger.warning("Insufficient stamina needed " + statusEffect.resourceCost);
				return false;
			}
			vitals.stamina -= statusEffect.resourceCost;
		} else if (statusEffect.resource == StatusEffect.Resource.ResourceMagic) {
			if (vitals.magic < statusEffect.resourceCost) {
				logger.warning("Insufficient magic needed " + statusEffect.resourceCost);
				return false;
			}
			vitals.magic -= statusEffect.resourceCost;
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

	private int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		int base = Common.randInt(statusEffect.minValue, statusEffect.maxValue);
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

	private void die(Vitals vitals) {
		vitals.health = 0;
		vitals.stamina = 0;
		vitals.magic = 0;
		vitals.dead = 1;
		vitals.changed = 1;
	}

	private void revive(Vitals vitals) {
		Vitals template = VitalsHandler.getTemplate(vitals);
		vitals.dead = 0;
		vitals.health = template.health;
		vitals.stamina = template.stamina;
		vitals.magic = template.magic;
		vitals.changed = 1;
	}

	private void updateVitals() {
		int regen;

		for (Vitals vitals : vitalsHandler.getVitals()) {
			if (vitals.subType == Vitals.SubType.Guard) {
				regen = 1000;
			} else {
				if ((System.currentTimeMillis() - vitals.lastCombat) <= 5000l) {
					regen = 2;
				} else {
					regen = 20;
				}
			}

			Vitals template = VitalsHandler.getTemplate(vitals);
			int stamina = template.stamina;
			int magic = template.magic;
			int health = template.health;

			if (vitals.dead == 1) {
				if (vitals.type == Vitals.VitalsType.Object) {
					continue;
				}

				if (deathTimer.containsKey(vitals.entityId)) {
					Long timeDead = deathTimer.get(vitals.entityId);
					Long timer = Common.deathTime;
					if ((System.currentTimeMillis() - timeDead) > timer) {
						revive(vitals);
						deathTimer.remove(vitals.entityId);
					}
				}
				continue;
			}

			if (vitals.health <= 0) {
				die(vitals);
				deathTimer.put(vitals.entityId, System.currentTimeMillis());
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

			if (vitals.type == Vitals.VitalsType.Character) {
				TrackData trackData = grid.get(vitals.entityId);
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

	private void sendVisualEffect(StatusEffect statusEffect, GmVector3 location) {
		GameMessage msg = new GameMessage();
		VisualEffect v = new VisualEffect();
		v.prefab = statusEffect.particleEffect;
		v.duration = statusEffect.ticks;
		v.startPosition = location;
		msg.setVisualEffect(v);
		for (TrackData trackData : grid.getAll()) {
			PlayerCommands.sendGameMessage(msg, trackData.id);
		}
	}

	private void sendStatusEffectResult(String statusEffectId, StatusEffectTarget statusEffectTarget,
			Vitals targetVitals, int value) {
		GameMessage msg = new GameMessage();
		StatusEffectResult result = new StatusEffectResult();
		result.originCharacterId = statusEffectTarget.originCharacterId;
		result.targetEntityId = targetVitals.entityId;
		result.targetCharacterId = targetVitals.characterId;
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
		List<Vitals> container;

		if (gridName.equals("build_objects")) {
			container = objectVitals();
			if (container.size() > 0) {
				for (TrackData trackData : grid.getAll()) {
					sendObjectVitals(trackData, container);
				}
			}

		} else {
			container = livingVitals();
			if (container.size() > 0) {
				for (TrackData trackData : grid.getAll()) {
					sendLivingVitals(trackData, container);
				}
			}
		}
	}

	private void sendObjectVitals(TrackData trackData, List<Vitals> container) {
		VitalsContainer toSend = new VitalsContainer();
		for (Vitals vitals : container) {
			TrackData vitalsTrackData = grid.get(vitals.entityId);

			if (Common.distance(vitalsTrackData, trackData) <= Common.vitalsDistance) {
				toSend.addVitals(vitals);
			}
		}

		GameMessage msg = new GameMessage();
		msg.vitalsContainer = toSend;
		PlayerCommands.sendGameMessage(msg, trackData.id);
	}

	private void sendLivingVitals(TrackData trackData, List<Vitals> container) {
		VitalsContainer toSend = new VitalsContainer();
		for (Vitals vitals : container) {
			TrackData vitalsTrackData = grid.get(vitals.entityId);

			if (Common.distance(vitalsTrackData, trackData) <= Common.vitalsDistance) {
				toSend.addVitals(vitals);
			}
		}

		GameMessage msg = new GameMessage();
		msg.vitalsContainer = toSend;
		PlayerCommands.sendGameMessage(msg, trackData.id);
	}

	private List<Vitals> objectVitals() {
		List<Vitals> container = new ArrayList<Vitals>();
		for (Vitals vitals : vitalsHandler.getVitals()) {
			if (vitals.changed == 1) {
				resetVitalTick(vitals.entityId);
				vitals.changed = 0;
			} else {
				int tick = nextVitalTick(vitals.entityId);
				if (tick >= 4) {
					continue;
				}
			}
			container.add(vitals);
		}
		return container;
	}

	private List<Vitals> livingVitals() {
		List<Vitals> container = new ArrayList<Vitals>();
		for (Vitals vitals : vitalsHandler.getVitals()) {
			Vitals template = VitalsHandler.getTemplate(vitals);
			if (vitals.changed == 1 || vitals.health < template.health || vitals.magic < template.magic
					|| vitals.stamina < template.stamina) {
				resetVitalTick(vitals.entityId);
				vitals.changed = 0;
			} else {
				int tick = nextVitalTick(vitals.entityId);
				if (tick >= 4) {
					continue;
				}
			}
			container.add(vitals);
		}
		return container;
	}

	@Override
	public void preStart() {
		tick(300L, "effects_tick");
		tick(1000L, "vitals_tick");
	}

	public void tick(long delay, String message) {
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}

}
