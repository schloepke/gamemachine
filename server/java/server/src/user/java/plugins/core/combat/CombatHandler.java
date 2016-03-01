package plugins.core.combat;

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.EntityTracking;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.SkillRequest;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Zone;
import plugins.landrush.BuildObjectHandler;

public class CombatHandler extends GameMessageActor {

    public static String name = CombatHandler.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void awake() {
    }

    @Override
    public void onTick(String message) {

    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        if (gameMessage.skillRequest != null) {
            useSkill(gameMessage.skillRequest);
        }
    }

    private void sendAttack(SkillRequest skillRequest, String zone) {
        GameMessage msg = new GameMessage();

        Grid grid = GridService.getInstance().getGrid(zone, "default");
        for (TrackData trackData : grid.getAll()) {
            if (EntityTracking.npcMap.containsKey(trackData.id)) {
                if (EntityTracking.npcMap.get(trackData.id).equals(playerId)) {
                    continue;
                }
            }

            if (!playerId.equals(trackData.id)) {
                msg.skillRequest = skillRequest;
                PlayerMessage.tell(msg, trackData.id);
            }
        }
    }

    private void ensureTargetVitals(SkillRequest.TargetType targetType, String entityId, String zone) {
        if (targetType == SkillRequest.TargetType.Character) {
            VitalsHandler.ensureCharacterVitals(entityId, zone);
        } else if (targetType == SkillRequest.TargetType.Object) {
            VitalsHandler.ensure(entityId, Vitals.VitalsType.Object, zone);
        } else if (targetType == SkillRequest.TargetType.BuildObject) {
            VitalsHandler.ensure(entityId, Vitals.VitalsType.BuildObject, zone);
        } else {
            throw new RuntimeException("Invalid target type " + targetType);
        }
    }

    private void useSkill(SkillRequest skillRequest) {
        boolean sendToObjectGrid = false;
        boolean sendToDefaultGrid = false;

        skillRequest.playerSkill = PlayerSkillHandler.getTemplate(skillRequest.playerSkillId);
        if (skillRequest.playerSkill == null) {
            logger.warning("Attack without player skill, ignoring");
            return;
        }

        Player player = PlayerService.getInstance().findByCharacterId(skillRequest.attackerCharacterId);
        if (player == null) {
            logger.warning("Unable to find player with character id " + skillRequest.attackerCharacterId);
            return;
        } else {
            skillRequest.originEntityId = player.id;
        }

        Zone zone = PlayerService.getInstance().getZone(skillRequest.originEntityId);

        // None is for non combat skills like warping to bind point.  Nothing implemented yet to handle these
        if (skillRequest.playerSkill.weaponType == PlayerSkill.WeaponType.None) {
            sendAttack(skillRequest, zone.name);
            return;
        }

        if (Strings.isNullOrEmpty(skillRequest.attackerCharacterId)) {
            logger.warning("Attack without attackerCharacterId, ignoring");
            return;
        }

        logger.warning("Attack " + skillRequest.attackerCharacterId + " " + skillRequest.targetId + " skill " + skillRequest.playerSkill.id + " " + skillRequest.playerSkill.statusEffects);

        if (skillRequest.relayOnly) {
            sendAttack(skillRequest, zone.name);
            return;
        }

        StatusEffectTarget statusEffectTarget = new StatusEffectTarget();
        VitalsHandler.ensureCharacterVitals(skillRequest.originEntityId, zone.name);

        statusEffectTarget.skillRequest = skillRequest;

        statusEffectTarget.originCharacterId = skillRequest.attackerCharacterId;
        statusEffectTarget.originEntityId = skillRequest.originEntityId;

        if (skillRequest.playerSkill.category == PlayerSkill.Category.SingleTarget) {
            if (Strings.isNullOrEmpty(skillRequest.targetId)) {
                logger.warning("SingleTarget with no targetId");
                return;
            } else {
                Character character = CharacterService.instance().find(skillRequest.targetId);

                // No character = Object/vehicle/etc..
                if (character == null) {
                    if (BuildObjectHandler.exists(skillRequest.targetId, zone.name)) {
                        skillRequest.targetType = SkillRequest.TargetType.BuildObject;
                    } else {
                        skillRequest.targetType = SkillRequest.TargetType.Object;
                    }
                    statusEffectTarget.targetEntityId = skillRequest.targetId;
                    sendToObjectGrid = true;
                } else {
                    skillRequest.targetType = SkillRequest.TargetType.Character;
                    statusEffectTarget.targetEntityId = character.playerId;
                    sendToDefaultGrid = true;
                }

                ensureTargetVitals(skillRequest.targetType, statusEffectTarget.targetEntityId, zone.name);
            }

        } else if (skillRequest.playerSkill.category == PlayerSkill.Category.Self) {
            skillRequest.targetType = SkillRequest.TargetType.Character;
            statusEffectTarget.targetEntityId = skillRequest.originEntityId;
            ensureTargetVitals(skillRequest.targetType, statusEffectTarget.targetEntityId, zone.name);
            sendToDefaultGrid = true;

        } else if (skillRequest.playerSkill.category == PlayerSkill.Category.Aoe || skillRequest.playerSkill.category == PlayerSkill.Category.AoeDot) {
            if (skillRequest.targetLocation == null) {
                logger.warning("Aoe without targetLocation");
                return;
            }

            statusEffectTarget.location = skillRequest.targetLocation;
            sendToObjectGrid = true;
            sendToDefaultGrid = true;

        } else if (skillRequest.playerSkill.category == PlayerSkill.Category.Pbaoe) {

            Grid grid = GridService.getInstance().getGrid(zone.name, "default");

            TrackData td = grid.get(skillRequest.originEntityId);
            if (td == null) {
                logger.warning("TrackData not found for " + skillRequest.originEntityId);
                return;
            }

            statusEffectTarget.location = new GmVector3();
            statusEffectTarget.location.xi = td.x;
            statusEffectTarget.location.yi = td.y;
            statusEffectTarget.location.zi = td.z;

            sendToObjectGrid = true;
            sendToDefaultGrid = true;
        } else {
            logger.warning("Invalid damage type");
            return;
        }

        if (sendToDefaultGrid) {
            StatusEffectManager.tell("default", zone.name, statusEffectTarget.clone(), getSelf());
        }

        if (sendToObjectGrid) {
            StatusEffectManager.tell("build_objects", zone.name, statusEffectTarget.clone(), getSelf());
        }

        sendAttack(skillRequest, zone.name);
    }

}
