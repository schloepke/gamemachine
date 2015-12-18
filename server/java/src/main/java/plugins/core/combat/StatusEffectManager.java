package plugins.core.combat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import io.gamemachine.chat.ChatSubscriptions;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.GameMachineLoader;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.messages.RegionInfo;
import plugins.landrush.BuildObjectHandler;
import scala.concurrent.duration.Duration;

public class StatusEffectManager extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(StatusEffectManager.class);
	public static String name = StatusEffectManager.class.getSimpleName();
	private Map<String, Long> deathTimer = new ConcurrentHashMap<String, Long>();
	private long deathTime = 15000L;
	private long outOfCombatTime = 10000L;
	public static List<GridSet> gridsets = new ArrayList<GridSet>();
	public static Set<String> handlerZones = new HashSet<String>();
	
	public static void tell(String gridName, String zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
		String actorName = null;
		if (!hasEffects(statusEffectTarget)) {
			logger.warn("No effects found for skill "+statusEffectTarget.skillRequest.playerSkill.id);
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
		if ( StatusEffectData.skillEffects.containsKey(statusEffectTarget.skillRequest.playerSkill.id)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static int passiveEffectCount(StatusEffectTarget statusEffectTarget) {
		int count = 0;
		for (StatusEffect statusEffect : StatusEffectData.skillEffects.get(statusEffectTarget.skillRequest.playerSkill.id)) {
			if (statusEffect.type == StatusEffect.Type.AttributeMaxDecrease || statusEffect.type == StatusEffect.Type.AttributeMaxIncrease) {
				count++;
			}
		}
		return count;
	}
	
	private static int activeEffectCount(StatusEffectTarget statusEffectTarget) {
		int count = 0;
		for (StatusEffect statusEffect : StatusEffectData.skillEffects.get(statusEffectTarget.skillRequest.playerSkill.id)) {
			if (statusEffect.type == StatusEffect.Type.AttributeDecrease || statusEffect.type == StatusEffect.Type.AttributeIncrease) {
				count++;
			}
		}
		return count;
	}

	public static int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
		return gameEntityManager.getEffectValue(statusEffect, playerSkill.id, characterId);
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
		gameEntityManager.skillUsed(playerSkill.id, characterId);
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
		createDefaultEffectHandlers();
	}
	
	private void createDefaultEffectHandlers() {
		
		int zoneCount = RegionInfo.db().findAll().size();
		
		if (zoneCount == 0) {
			logger.warn("Combat system requires at least one zone configured");
			return;
		}
		
		for (Zone zone : ZoneService.staticZones()) {
			createEffectHandler(zone.name);
		}
	}
	
	public static void createEffectHandler(String zone) {
		if (handlerZones.contains(zone)) {
			logger.warn("effect handler already created for zone "+zone);
			return;
		}
		
		GridSet gridSet = new GridSet();
		gridSet.zone = zone;
		
		GridService.getInstance().createForZone(zone);
		gridSet.playerGrid = GridService.getInstance().getGrid(zone,"default");
		
		gridSet.objectGrid = GridService.getInstance().getGrid(zone,"build_objects");
		
		gridsets.add(gridSet);
		
		GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "default", zone),
				ActiveEffectHandler.actorName("default", zone));
		GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "build_objects", zone),
				ActiveEffectHandler.actorName("build_objects", zone));
		
		GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "default", zone),
				PassiveEffectHandler.actorName("default", zone));
		GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "build_objects", zone),
				PassiveEffectHandler.actorName("build_objects", zone));
		
		handlerZones.add(zone);
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
				if (vitalsProxy.vitals.type != Vitals.VitalsType.BuildObject) {
					deathTimer.put(vitalsProxy.vitals.entityId, System.currentTimeMillis());
				}
				
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
