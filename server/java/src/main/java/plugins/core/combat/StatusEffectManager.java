package plugins.core.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMachineLoader;
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
	
	public static void tell(StatusEffectTarget statusEffectTarget, ActorRef sender) {
		ActorSelection sel = ActorUtil.getSelectionByName(name);
		sel.tell(statusEffectTarget, sender);
	}
	
	public static void tell(String gridName, int zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
		String actorName = null;
		if (statusEffectTarget.attack.playerSkill.skillType == PlayerSkill.SkillType.Active) {
			actorName = ActiveEffectHandler.actorName(gridName, zone);
		} else {
			actorName = PassiveEffectHandler.actorName(gridName, zone);
		}
		ActorSelection sel = ActorUtil.getSelectionByName(actorName);
		sel.tell(statusEffectTarget, sender);
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
		if (message instanceof StatusEffectTarget) {
			StatusEffectTarget statusEffectTarget = (StatusEffectTarget)message;
			
		} else if (message instanceof String) {
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
