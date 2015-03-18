package pvp_game;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.DataRequest;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectResult;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vector3;
import io.gamemachine.messages.VisualEffect;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StatusEffectHandler extends UntypedActor {

	private static ConcurrentHashMap<String, Integer> vitalTicks = new ConcurrentHashMap<String, Integer>();
	private static ConcurrentHashMap<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();

	public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
	public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

	public static ConcurrentHashMap<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();

	public static AtomicLong counter = new AtomicLong();
	public static String name = StatusEffectHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static void createStatusEffects() {
		statusEffects = new ConcurrentHashMap<String, StatusEffect>();
		skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

		createStatusEffect("deadly_attack", StatusEffect.Type.AttributeDecrease, "deadly_attack", 1, "health", 500,
				1000, null);
		createStatusEffect("light_attack", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);

		createStatusEffect("cleave", StatusEffect.Type.AttributeDecrease, "cleave", 1, "health", 50, 100, null);
		createStatusEffect("charge", StatusEffect.Type.AttributeDecrease, "charge", 1, "health", 100, 100, null);

		createStatusEffect("poison_arrow", StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 50, 100, null);
		createStatusEffect("poison_arrow", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);

		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 50, 100, null);
		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,
				null);
		createStatusEffect("lightning_bolt", StatusEffect.Type.AttributeDecrease, "lightning_bolt", 1, "health", 200,
				400, "Thunder");
		createStatusEffect("staff_heal", StatusEffect.Type.AttributeIncrease, "healing_ring", 5, "health", 100, 200,
				"Eternal Light");
		createStatusEffect("fire_field", StatusEffect.Type.AttributeDecrease, "fire_field", 5, "health", 50, 100,
				"Eternal Flame");
		createStatusEffect("catapult_explosive", StatusEffect.Type.AttributeDecrease, "catapult_explosive", 1,
				"health", 200, 300, null);

	}

	private static void createStatusEffect(String skill, StatusEffect.Type type, String id, int ticks,
			String attribute, int minValue, int maxValue, String particleEffect) {
		StatusEffect e = new StatusEffect();
		e.type = type;
		e.id = id;
		e.ticks = ticks;
		e.attribute = attribute;
		e.minValue = minValue;
		e.maxValue = maxValue;
		e.particleEffect = particleEffect;
		if (!skillEffects.containsKey(skill)) {
			skillEffects.put(skill, new ArrayList<StatusEffect>());
		}
		skillEffects.get(skill).add(e);
		statusEffects.put(id, e);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget target = (StatusEffectTarget) message;
			setStatusEffects(target);
			applyStatusEffects(target);
			target.lastTick = System.currentTimeMillis();
			sendVitals();
		} else if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				updateVitals();
				tick(1000L, "vitals_tick");
			} else if (message.equals("effects_tick")) {
				for (StatusEffectTarget target : targets.values()) {
					if ((System.currentTimeMillis() - target.lastTick) >= 1000l) {
						//logger.warning("Tick " + target.skill);
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

	private void setStatusEffects(StatusEffectTarget target) {
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

	private void applyStatusEffects(StatusEffectTarget statusEffectTarget) {
		for (StatusEffect effect : statusEffectTarget.statusEffect) {
			if (effect.ticks > statusEffectTarget.ticks) {
				if (statusEffectTarget.target.equals("aoe")) {
					if (statusEffectTarget.ticks == 0) {
						sendVisualEffect(effect, statusEffectTarget.location);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range,
							statusEffectTarget.location, "default")) {
						applyStatusEffect(statusEffectTarget.origin, target, effect);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range,
							statusEffectTarget.location, "world_objects")) {
						applyStatusEffect(statusEffectTarget.origin, target, effect);
					}
				} else {
					applyStatusEffect(statusEffectTarget.origin, statusEffectTarget.target, effect);
				}
			}
		}
		statusEffectTarget.ticks++;
	}

	private int applyStatusEffectToWorldObject(String origin, String target, StatusEffect effect) {

		if (effect.type == StatusEffect.Type.AttributeDecrease) {
			WorldObject worldObject = ConsumableItemHandler.wobjects.get(target);
			int damage = getEffectValue(effect);
			if (damage > 0) {
				damage = damage / 4;
			}
			worldObject.health -= damage;
			logger.warning("applyStatusEffectToWorldObject " + target + " damage " + damage);

			if (worldObject.health <= 0) {
				if (worldObject.hasPlayerItemId()) {
					ConsumableItemHandler.remove(worldObject.id);
				}
			} else {
				WorldObject.db().saveAsync(worldObject);
			}

			return damage;
		} else {
			return 0;
		}

	}

	private int applyStatusEffect(String origin, String target, StatusEffect effect) {
		if (ConsumableItemHandler.wobjects.containsKey(target)) {
			return applyStatusEffectToWorldObject(origin, target, effect);
		}

		Vitals vitals = getVitals(target);
		if (vitals.dead == 1) {
			return 0;
		}
		vitals.lastCombat = System.currentTimeMillis();

		Vitals originVitals = getVitals(origin);
		originVitals.lastCombat = System.currentTimeMillis();

		int targetBaseHealth = CharacterHandler.currentHealth(vitals.id);
		int value = getEffectValue(effect);

		if (effect.type == StatusEffect.Type.AttributeDecrease) {
			if (vitals.id.equals(origin)) {
				return 0;
			}
			vitals.health -= value;
			if (vitals.health < 0) {
				vitals.health = 0;
			}
		} else if (effect.type == StatusEffect.Type.AttributeIncrease) {
			vitals.health += value;
			if (vitals.health > targetBaseHealth) {
				vitals.health = targetBaseHealth;
			}
		}

		if (value > 0) {
			//logger.warning("target " + vitals.id + " damage " + value + " type " + effect.type + " health "+ vitals.health);
			sendStatusEffectResult(effect.id, origin, target, value);
		}

		return value;
	}

	private int getEffectValue(StatusEffect effect) {
		return Common.randInt(effect.minValue, effect.maxValue);
	}

	private Vitals getVitals(String id) {
		if (!CombatHandler.playerVitals.containsKey(id)) {
			Vitals vitals = new Vitals();
			vitals.id = id;
			vitals.dead = 0;
			vitals.health = CharacterHandler.currentHealth(id);
			vitals.stamina = CharacterHandler.currentStamina(id);
			vitals.magic = CharacterHandler.currentMagic(id);
			CombatHandler.playerVitals.put(vitals.id, vitals);
		}
		return CombatHandler.playerVitals.get(id);
	}

	private void updateVitals() {
		int regen;
		for (Vitals vitals : CombatHandler.playerVitals.values()) {
			if (vitals.id.startsWith("npc_guard")) {
				regen = 1000;
			} else {
				if ((System.currentTimeMillis() - vitals.lastCombat) <= 5000l) {
					regen = 2;
				} else {
					regen = 20;
				}
			}
			

			int stamina = CharacterHandler.currentStamina(vitals.id);
			int magic = CharacterHandler.currentMagic(vitals.id);
			int health = CharacterHandler.currentHealth(vitals.id);

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
						vitals.dead = 0;
						vitals.health = health;
						vitals.stamina = stamina;
						vitals.magic = magic;
						deathTimer.remove(vitals.id);
					}
				}
				continue;
			}

			if (vitals.health <= 0) {
				vitals.health = 0;
				vitals.stamina = 0;
				vitals.magic = 0;
				vitals.dead = 1;
				deathTimer.put(vitals.id, System.currentTimeMillis());
				continue;
			}

			if (vitals.health < health) {
				vitals.health += regen;
				if (vitals.health > health) {
					vitals.health = health;
				}
			}

			if (vitals.stamina < stamina) {
				vitals.stamina += regen;
				if (vitals.stamina > stamina) {
					vitals.stamina = stamina;
				}
			}

			if (vitals.magic < magic) {
				vitals.magic += regen;
				if (vitals.magic > magic) {
					vitals.magic = magic;
				}
			}
		}
		sendVitals();
	}

	private void sendVisualEffect(StatusEffect effect, Vector3 location) {
		GameMessage msg = new GameMessage();
		VisualEffect v = new VisualEffect();
		v.prefab = effect.particleEffect;
		v.duration = effect.ticks;
		v.startPosition = location;
		msg.setVisualEffect(v);
		Grid grid = GameGrid.getGameGrid("mygame", "default");
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

		Grid grid = GameGrid.getGameGrid("mygame", "default");
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
		List<GameMessage> toSend = new ArrayList<GameMessage>();

		for (Vitals vitals : CombatHandler.playerVitals.values()) {
			int health = CharacterHandler.currentHealth(vitals.id);
			Boolean send = false;
			if (vitals.health < health) {
				resetVitalTick(vitals.id);
				send = true;
			} else {
				int tick = nextVitalTick(vitals.id);
				if (tick < 4) {
					send = true;
					//logger.warning("vitals tick " + vitals.id + " " + tick + " health " + vitals.health);
				}
			}
			if (send) {
				GameMessage msg = new GameMessage();
				msg.vitals = vitals;
				toSend.add(msg);
			}
		}

		Grid grid = GameGrid.getGameGrid("mygame", "default");
		for (TrackData trackData : grid.getAll()) {
			for (GameMessage msg : toSend) {
				PlayerCommands.sendGameMessage(msg, trackData.id);
			}
		}
	}

	@Override
	public void preStart() {
		createStatusEffects();
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
