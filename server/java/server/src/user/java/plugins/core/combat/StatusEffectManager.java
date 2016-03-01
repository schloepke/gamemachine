package plugins.core.combat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.CombatLog;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.RegionInfo;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Zone;
import io.gamemachine.regions.ZoneService;

public class StatusEffectManager extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(StatusEffectManager.class);
    public static String name = StatusEffectManager.class.getSimpleName();

    public static boolean combatLogEnabled = true;

    public static List<GridSet> gridsets = new ArrayList<GridSet>();
    public static Set<String> handlerZones = new HashSet<String>();

    public static void tell(String gridName, String zone, StatusEffectTarget statusEffectTarget, ActorRef sender) {
        String actorName = null;
        if (!hasEffects(statusEffectTarget)) {
            logger.warn("No effects found for skill " + statusEffectTarget.skillRequest.playerSkill.id);
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
        if (StatusEffectData.skillEffects.containsKey(statusEffectTarget.skillRequest.playerSkill.id)) {
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
            if (vitalsProxy.get("stamina") < statusEffect.resourceCost) {
                logger.warn("Insufficient stamina needed " + statusEffect.resourceCost);
                return false;
            }
            vitalsProxy.subtract("stamina", statusEffect.resourceCost);
        } else if (statusEffect.resource == StatusEffect.Resource.ResourceMagic) {
            if (vitalsProxy.get("magic") < statusEffect.resourceCost) {
                logger.warn("Insufficient magic needed " + statusEffect.resourceCost);
                return false;
            }
            vitalsProxy.subtract("magic", statusEffect.resourceCost);
        }
        return true;
    }

    public static void sendCombatDamage(VitalsProxy origin, VitalsProxy target, int value, CombatLog.Type type, String zone) {
        GameEntityManager gameEntityManager = GameEntityManagerService.instance().getGameEntityManager();
        gameEntityManager.combatDamage(origin, target, value, type);

        if (!combatLogEnabled) {
            return;
        }

        GameMessage msg = new GameMessage();

        msg.combatLog = new CombatLog();
        msg.combatLog.origin = origin.getEntityId();
        msg.combatLog.target = target.getEntityId();
        msg.combatLog.value = value;
        msg.combatLog.type = type;

        Grid grid = GridService.getInstance().getGrid(zone, "default");

        TrackData trackData = grid.get(msg.combatLog.origin);
        if (trackData != null) {
            PlayerMessage.tell(msg, trackData.id);
        }

        trackData = grid.get(msg.combatLog.target);
        if (trackData != null) {
            PlayerMessage.tell(msg, trackData.id);
        }
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
            logger.warn("effect handler already created for zone " + zone);
            return;
        }

        GridSet gridSet = new GridSet();
        gridSet.zone = zone;

        GridService.getInstance().createForZone(zone);
        gridSet.playerGrid = GridService.getInstance().getGrid(zone, "default");

        gridSet.objectGrid = GridService.getInstance().getGrid(zone, "build_objects");

        gridsets.add(gridSet);

        GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "default", zone),
                ActiveEffectHandler.actorName("default", zone));
        GameMachineLoader.getActorSystem().actorOf(Props.create(ActiveEffectHandler.class, "build_objects", zone),
                ActiveEffectHandler.actorName("build_objects", zone));

        GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "default", zone),
                PassiveEffectHandler.actorName("default", zone));
        GameMachineLoader.getActorSystem().actorOf(Props.create(PassiveEffectHandler.class, "build_objects", zone),
                PassiveEffectHandler.actorName("build_objects", zone));

        GameMachineLoader.getActorSystem().actorOf(Props.create(VitalsRegen.class, zone),
                VitalsRegen.actorName(zone));


        handlerZones.add(zone);
    }


    @Override
    public void onReceive(Object arg0) throws Exception {
        // TODO Auto-generated method stub

    }

}
