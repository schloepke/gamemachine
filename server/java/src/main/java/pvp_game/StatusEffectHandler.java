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

	public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
	public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();
	
	public static ConcurrentHashMap<Long, StatusEffectTarget> targets = new ConcurrentHashMap<Long, StatusEffectTarget>();
	
	public static AtomicLong counter = new AtomicLong();
	public static String name = StatusEffectHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	

	public static void createStatusEffects() {
		statusEffects = new ConcurrentHashMap<String, StatusEffect>();
		skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();
		
		createStatusEffect("cleave", StatusEffect.Type.AttributeDecrease, "cleave", 1, "health", 50, 100,null);
		createStatusEffect("charge", StatusEffect.Type.AttributeDecrease, "charge", 1, "health", 100, 100,null);
		
		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "poison", 8, "health", 50, 100,null);
		createStatusEffect("poison_blade", StatusEffect.Type.AttributeDecrease, "light_attack", 1, "health", 50, 100,null);
		createStatusEffect("lightning_bolt", StatusEffect.Type.AttributeDecrease, "lightning_bolt", 1, "health", 200,
				400,"Thunder");
		createStatusEffect("staff_heal", StatusEffect.Type.AttributeIncrease, "healing_ring", 5, "health", 100, 200,"Eternal Light");
		createStatusEffect("fire_field", StatusEffect.Type.AttributeDecrease, "fire_field", 5, "health", 50, 100,"Eternal Flame");
		createStatusEffect("catapult_explosive", StatusEffect.Type.AttributeDecrease, "catapult_explosive", 1, "health", 200, 300,null);

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
			StatusEffectTarget target = (StatusEffectTarget)message;
			setStatusEffects(target);
			applyStatusEffects(target);
			target.lastTick = System.currentTimeMillis();
			sendVitals();
		} else if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				tick(1000L, "vitals_tick");
				updateVitals();
			} else if (message.equals("effects_tick")) {
				for (StatusEffectTarget target : targets.values()) {
					if ((System.currentTimeMillis() - target.lastTick) >= 1000l) {
						logger.warning("Tick "+target.skill);
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
			DataRequest request = (DataRequest)message;
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
			targets.put(target.activeId,target);
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
						sendVisualEffect(effect,statusEffectTarget.location);
					}
					for (String target : Common.getTargetsInRange(statusEffectTarget.range,statusEffectTarget.location,"default")) {
						applyStatusEffect(statusEffectTarget.origin,target, effect);
					}
				} else {
					applyStatusEffect(statusEffectTarget.origin,statusEffectTarget.target, effect);
				}
			}
		}
		statusEffectTarget.ticks++;
	}

	
	private int applyStatusEffect(String origin, String target, StatusEffect effect) {
		Vitals originVitals = getVitals(origin);
		originVitals.lastCombat = System.currentTimeMillis();
		Vitals vitals = getVitals(target);
		vitals.lastCombat = System.currentTimeMillis();
		
		int health = CharacterHandler.currentHealth(origin);
		int value = getEffectValue(effect);
		logger.warning("damage "+value+" type "+effect.type);
		if (effect.type == StatusEffect.Type.AttributeDecrease) {
			if (vitals.id.equals(origin)) {
				return 0;
			}
			vitals.health -= getEffectValue(effect);
			if (vitals.health < 0) {
				vitals.health = 0;
			}
		} else if (effect.type == StatusEffect.Type.AttributeIncrease) {
			vitals.health += getEffectValue(effect);
			if (vitals.health > health) {
				vitals.health = health;
			}
		}
		if (value > 0) {
			sendStatusEffectResult(effect.id,origin,target,value);
		}
		
		return value;
	}
	
	private int getEffectValue(StatusEffect effect) {
		return Common.randInt(effect.minValue,effect.maxValue);
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
			if ((System.currentTimeMillis() - vitals.lastCombat) <= 5000l) {
				regen = 2;
			} else {
				regen = 20;
			}
			
			int stamina = CharacterHandler.currentStamina(vitals.id);
			int magic = CharacterHandler.currentMagic(vitals.id);
			int health = CharacterHandler.currentHealth(vitals.id);
						
			if (vitals.dead == 1) {
				vitals.dead =0;
				vitals.health = health;
				vitals.stamina = stamina;
				vitals.magic = magic;
				continue;
			}
			
			if (vitals.health <= 0) {
				vitals.health = 0;
				vitals.stamina = 0;
				vitals.magic = 0;
				vitals.dead = 1;
				continue;
			}
			
			if (vitals.health < health) {
				vitals.health += regen;
				if (vitals.health > health) {
					vitals.health = health;
				}
			}
			
			
			if (vitals.stamina < stamina) {
				vitals.stamina +=regen;
				if (vitals.stamina > stamina) {
					vitals.stamina = stamina;
				}
			}
			
			
			if (vitals.magic < magic) {
				vitals.magic +=regen;
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
	
	private void sendVitals() {
		Grid grid = GameGrid.getGameGrid("mygame", "default");
		for (TrackData trackData : grid.getAll()) {
			for (Vitals vitals : CombatHandler.playerVitals.values()) {
				//if (vitals.health < maxHealth) {
					GameMessage msg = new GameMessage();
					msg.vitals = vitals;
					PlayerCommands.sendGameMessage(msg, trackData.id);
				//}
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
