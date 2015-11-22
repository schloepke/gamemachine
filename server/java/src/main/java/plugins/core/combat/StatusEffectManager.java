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
import io.gamemachine.chat.ChatSubscriptions;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.grid.GameGrid;
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
	private long outOfCombatTime = 10000L;
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
		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		return gameEntityManager.getEffectValue(statusEffect, playerSkill, characterId);
	}

	public static boolean inGroup(String playerId) {
		String playerGroup = ChatSubscriptions.playerGroup(playerId);
		if (playerGroup.equals("nogroup")) {
			return false;
		} else {
			return true;
		}
	}

	public static void skillUsed(PlayerSkill playerSkill, String characterId) {
		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		gameEntityManager.skillUsed(playerSkill, characterId);
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
				logger.debug("Insufficient stamina needed " + statusEffect.resourceCost);
				return false;
			}
			vitalsProxy.vitals.stamina -= statusEffect.resourceCost;
		} else if (statusEffect.resource == StatusEffect.Resource.ResourceMagic) {
			if (vitalsProxy.vitals.magic < statusEffect.resourceCost) {
				logger.debug("Insufficient magic needed " + statusEffect.resourceCost);
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
			gridSet.zone = i;
			
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
				if (vitalsProxy.vitals.type == Vitals.VitalsType.BuildObject) {
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

			int healthRegen = vitalsProxy.baseVitals.healthRegen;
			int magicRegen = vitalsProxy.baseVitals.magicRegen;
			int staminaRegen = vitalsProxy.baseVitals.staminaRegen;
			
			long lastCombat = System.currentTimeMillis() - vitalsProxy.vitals.lastCombat;
			if (lastCombat < outOfCombatTime && vitalsProxy.baseVitals.combatRegenMod > 0) {
				vitalsProxy.vitals.inCombat = true;
				
				healthRegen = Math.round(healthRegen * (vitalsProxy.baseVitals.combatRegenMod / 100f));
				magicRegen = Math.round(magicRegen * (vitalsProxy.baseVitals.combatRegenMod/100f));
				staminaRegen = Math.round(staminaRegen * (vitalsProxy.baseVitals.combatRegenMod/100f));
				
			} else {
				vitalsProxy.vitals.inCombat = false;
			}
			
			if (vitalsProxy.vitals.health < health && healthRegen > 0) {
				vitalsProxy.vitals.health += healthRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.health > health) {
					vitalsProxy.vitals.health = health;
				}
				if (vitalsProxy.vitals.type == Vitals.VitalsType.BuildObject) {
					BuildObjectHandler.setHealth(vitalsProxy.vitals.entityId, vitalsProxy.vitals.health);
				}
			}

			if (vitalsProxy.vitals.stamina < stamina && staminaRegen > 0) {
				vitalsProxy.vitals.stamina += staminaRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.stamina > stamina) {
					vitalsProxy.vitals.stamina = stamina;
				}
			}

			if (vitalsProxy.vitals.magic < magic && magicRegen > 0) {
				vitalsProxy.vitals.magic += magicRegen;
				vitalsProxy.vitals.changed = 1;
				if (vitalsProxy.vitals.magic > magic) {
					vitalsProxy.vitals.magic = magic;
				}
			}

		}
	}
	
	private void die(VitalsProxy vitalsProxy) {
		logger.warn("Die "+vitalsProxy.vitals.entityId);
		vitalsProxy.set("health", 0);
		vitalsProxy.set("stamina", 0);
		vitalsProxy.set("magic", 0);
		vitalsProxy.vitals.dead = 1;
		vitalsProxy.vitals.changed = 1;
		vitalsProxy.vitals.inCombat = false;
	}

	private void revive(VitalsProxy vitalsProxy) {
		logger.warn("Revive "+vitalsProxy.vitals.entityId);
		vitalsProxy.vitals.dead = 0;
		vitalsProxy.setToBase("health");
		vitalsProxy.setToBase("stamina");
		vitalsProxy.setToBase("magic");
		vitalsProxy.vitals.changed = 1;
	}
	
}