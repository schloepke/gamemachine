package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.ZoneInfo;
import plugins.landrush.BuildObjectHandler;
import scala.concurrent.duration.Duration;

public class StatusEffectManager extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(StatusEffectManager.class);
	public static String name = StatusEffectManager.class.getSimpleName();
	private Map<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();
	private long deathTime = 15000L;
	public static List<GridSet> gridsets = new ArrayList<GridSet>();
	
	
	public static void tell(String gridName, int zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
		String actorName = null;
		if (!hasEffects(statusEffectTarget)) {
			logger.warn("No effects found for skill "+statusEffectTarget.attack.playerSkill.id);
			return;
		}
		
		if (activeEffectCount(statusEffectTarget) > 0) {
			actorName = ActiveEffectHandler.actorName(gridName, zone);
			ActorSelection sel = ActorUtil.getSelectionByName(actorName);
			sel.tell(statusEffectTarget.clone(), sender);
		}
		
		if (passiveEffectCount(statusEffectTarget) > 0) {
			actorName = PassiveEffectHandler.actorName(gridName, zone);
			ActorSelection sel = ActorUtil.getSelectionByName(actorName);
			sel.tell(statusEffectTarget.clone(), sender);
		}
		
	}
	

	private static boolean hasEffects(StatusEffectTarget statusEffectTarget) {
		if ( StatusEffectData.skillEffects.containsKey(statusEffectTarget.attack.playerSkill.id)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static int passiveEffectCount(StatusEffectTarget statusEffectTarget) {
		int count = 0;
		for (StatusEffect statusEffect : StatusEffectData.skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
			if (statusEffect.type == StatusEffect.Type.AttributeMaxDecrease || statusEffect.type == StatusEffect.Type.AttributeMaxIncrease) {
				count++;
			}
		}
		return count;
	}
	
	private static int activeEffectCount(StatusEffectTarget statusEffectTarget) {
		int count = 0;
		for (StatusEffect statusEffect : StatusEffectData.skillEffects.get(statusEffectTarget.attack.playerSkill.id)) {
			if (statusEffect.type == StatusEffect.Type.AttributeDecrease || statusEffect.type == StatusEffect.Type.AttributeIncrease) {
				count++;
			}
		}
		return count;
	}

	public static int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		int base = randInt(statusEffect.minValue, statusEffect.maxValue);
		Character character = CharacterService.instance().find(characterId);
		int level = skillLevel(playerSkill.id, character.id);
		return base + level;
	}

	public static void skillUp(PlayerSkill playerSkill, String characterId) {
		int chance = randInt(1, 10);
		if (chance < 5) {
			return;
		}

		Character character = CharacterService.instance().find(characterId);
		if (character == null) {
			logger.warn("Unable to find character for " + characterId);
			return;
		}

		if (PlayerSkillHandler.hasPlayerSkill(playerSkill.id, character.id)) {
			PlayerSkill existing = PlayerSkillHandler.playerSkill(playerSkill.id, character.id);
			existing.level += 1;
			PlayerSkillHandler.savePlayerSkill(existing);
			logger.warn("Skill up " + playerSkill.id + " level " + existing.level);
		}
	}
	// Can be a skill or player item
	public static int skillLevel(String id, String characterId) {
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
	
	public static boolean inGroup(String playerId) {
		String playerGroup = ChatSubscriptions.playerGroup(playerId);
		if (playerGroup.equals("nogroup")) {
			return false;
		} else {
			return true;
		}
	}

	public static String playerGroup(String playerId) {
		return ChatSubscriptions.playerGroup(playerId);
	}

	public static boolean inSameGroup(String playerId, String otherId) {
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
	
	public static boolean DeductCost(VitalsProxy vitalsProxy, StatusEffect statusEffect) {
		if (statusEffect.resourceCost == 0) {
			return true;
		}

		if (statusEffect.resource == StatusEffect.Resource.ResourceStamina) {
			if (vitalsProxy.vitals.stamina < statusEffect.resourceCost) {
				logger.warn("Insufficient stamina needed " + statusEffect.resourceCost);
				return false;
			}
			vitalsProxy.vitals.stamina -= statusEffect.resourceCost;
		} else if (statusEffect.resource == StatusEffect.Resource.ResourceMagic) {
			if (vitalsProxy.vitals.magic < statusEffect.resourceCost) {
				logger.warn("Insufficient magic needed " + statusEffect.resourceCost);
				return false;
			}
			vitalsProxy.vitals.magic -= statusEffect.resourceCost;
		}
		return true;
	}
	
	public StatusEffectManager() {
		createEffectHandlers();
	}
	
	private void createEffectHandlers() {
		
		int zoneCount = ZoneInfo.db().findAll().size();
		
		if (zoneCount == 0) {
			logger.warn("Combat system requires at least one zone configured");
			return;
		}
		
		
		for (int i = 0; i < zoneCount; i++) {
			GridSet gridSet = new GridSet();
			gridSet.zone = 0;
			
			gridSet.playerGrid = GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "default" + i);
			
			gridSet.objectGrid = GameGrid.loadGameGrid(AppConfig.getDefaultGameId(), "build_objects" + i);
			
			gridsets.add(gridSet);
			
			GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "default", i),
					ActiveEffectHandler.actorName("default", i));
			GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "build_objects", i),
					ActiveEffectHandler.actorName("build_objects", i));
			
			GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "default", i),
					PassiveEffectHandler.actorName("default", i));
			GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "build_objects", i),
					PassiveEffectHandler.actorName("build_objects", i));

		}
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			if (message.equals("vitals_tick")) {
				updateVitals();
				tick(1000L, "vitals_tick");
			}
		}
	}

	@Override
	public void preStart() {
		tick(1000L, "vitals_tick");
	}

	public void tick(long delay, String message) {
		getContext().system().scheduler().scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(),
				message, getContext().dispatcher(), null);
	}
	
	private void updateVitals() {

		for (VitalsProxy vitalsProxy : VitalsHandler.getVitals()) {

			int stamina = vitalsProxy.baseVitals.stamina;
			int magic = vitalsProxy.baseVitals.magic;
			int health = vitalsProxy.baseVitals.health;

			if (vitalsProxy.vitals.dead == 1) {
				if (vitalsProxy.vitals.type == Vitals.VitalsType.Object) {
					BuildObjectHandler.setHealth(vitalsProxy.vitals.entityId, vitalsProxy.vitals.health);
					VitalsHandler.remove(vitalsProxy.vitals.entityId);
					continue;
				}

				if (deathTimer.containsKey(vitalsProxy.vitals.entityId)) {
					Long timeDead = deathTimer.get(vitalsProxy.vitals.entityId);
					Long timer = deathTime;
					if ((System.currentTimeMillis() - timeDead) > timer) {
						revive(vitalsProxy);
						deathTimer.remove(vitalsProxy.vitals.entityId);
					}
				}
				continue;
			}

			if (vitalsProxy.vitals.health <= 0) {
				die(vitalsProxy);
				deathTimer.put(vitalsProxy.vitals.entityId, System.currentTimeMillis());
				continue;
			}

			if (vitalsProxy.vitals.health < health) {
				vitalsProxy.vitals.health += vitalsProxy.vitals.healthRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.health > health) {
					vitalsProxy.vitals.health = health;
				}
			}

			if (vitalsProxy.vitals.stamina < stamina) {
				vitalsProxy.vitals.stamina += vitalsProxy.vitals.staminaRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.stamina > stamina) {
					vitalsProxy.vitals.stamina = stamina;
				}
			}

			if (vitalsProxy.vitals.magic < magic) {
				vitalsProxy.vitals.magic += vitalsProxy.vitals.magicRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.magic > magic) {
					vitalsProxy.vitals.magic = magic;
				}
			}

		}
	}
	
	private void die(VitalsProxy vitalsProxy) {
		vitalsProxy.set("health", 0);
		vitalsProxy.set("stamina", 0);
		vitalsProxy.set("magic", 0);
		vitalsProxy.vitals.dead = 1;
		vitalsProxy.vitals.changed = 1;
	}

	private void revive(VitalsProxy vitalsProxy) {
		vitalsProxy.vitals.dead = 0;
		vitalsProxy.setToBase("health");
		vitalsProxy.setToBase("stamina");
		vitalsProxy.setToBase("magic");
		vitalsProxy.vitals.changed = 1;
	}
	
}
