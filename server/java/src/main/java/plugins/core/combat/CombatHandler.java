package plugins.core.combat;

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.GmVector3;
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
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.attack != null) {
				doAttack(gameMessage.attack);
				setReply(gameMessage);
			}
		}
	}

	private void sendAttack(Attack attack, int zone) {
		GameMessage msg = new GameMessage();

		Grid grid = GridService.getInstance().getGrid(zone, "default");
		for (TrackData trackData : grid.getAll()) {
			if (!playerId.equals(trackData.id)) {
				msg.attack = attack;
				PlayerMessage.tell(msg, trackData.id);
			}
		}
	}
	
	private void ensureTargetVitals(Attack.TargetType targetType, String entityId, int zone) {
		if (targetType == Attack.TargetType.Character) {
			 VitalsHandler.ensure(entityId, zone);
		} else if (targetType == Attack.TargetType.Object) {
			VitalsHandler.ensure(entityId,	Vitals.VitalsType.Object, zone);
		} else if (targetType == Attack.TargetType.BuildObject) {
			VitalsHandler.ensure(entityId,	Vitals.VitalsType.BuildObject, zone);
		} else {
			throw new RuntimeException("Invalid target type " + targetType);
		}
	}
	
	private void doAttack(Attack attack) {
		boolean sendToObjectGrid = false;
		boolean sendToDefaultGrid = false;
		
		Zone zone =  PlayerService.getInstance().getZone(playerId);
		if (attack.playerSkill == null) {
			logger.warning("Attack without player skill, ignoring");
			return;
		}

		if (Strings.isNullOrEmpty(attack.attackerCharacterId)) {
			logger.warning("Attack without attackerCharacterId, ignoring");
			return;
		}
		
		logger.warning("Attack " + attack.attackerCharacterId + " " + attack.targetId + " skill " + attack.playerSkill.id);
		
		StatusEffectTarget statusEffectTarget = new StatusEffectTarget();
		VitalsHandler.ensure(playerId, zone.number);
		
		statusEffectTarget.attack = attack;
		
		statusEffectTarget.originCharacterId = attack.attackerCharacterId;
		statusEffectTarget.originEntityId = playerId;
				
		if (attack.playerSkill.category == PlayerSkill.Category.SingleTarget) {
			if (Strings.isNullOrEmpty(attack.targetId)) {
				logger.warning("SingleTarget with no targetId");
				return;
			} else {
				Character character = CharacterService.instance().find(attack.targetId);
				
				// No character = Object/vehicle/etc..
				if (character == null) {
					if (BuildObjectHandler.exists(attack.targetId)) {
						attack.targetType = Attack.TargetType.BuildObject;
					} else {
						attack.targetType = Attack.TargetType.Object;
					}
					statusEffectTarget.targetEntityId = attack.targetId;
					sendToObjectGrid = true;
				} else {
					attack.targetType = Attack.TargetType.Character;
					statusEffectTarget.targetEntityId = character.playerId;
					sendToDefaultGrid = true;
				}
				
				ensureTargetVitals(attack.targetType, statusEffectTarget.targetEntityId, zone.number);
			}
			
		} else if (attack.playerSkill.category == PlayerSkill.Category.Self) {
			attack.targetType = Attack.TargetType.Character;
			statusEffectTarget.targetEntityId = playerId;
			ensureTargetVitals(attack.targetType, statusEffectTarget.targetEntityId, zone.number);
			sendToDefaultGrid = true;
			
		} else if (attack.playerSkill.category == PlayerSkill.Category.Aoe || attack.playerSkill.category == PlayerSkill.Category.AoeDot) {
			if (attack.targetLocation == null) {
				logger.warning("Aoe without targetLocation");
				return;
			}
			
			statusEffectTarget.location = attack.targetLocation;
			sendToObjectGrid = true;
			sendToDefaultGrid = true;
			
		} else if (attack.playerSkill.category == PlayerSkill.Category.Pbaoe) {
			
			Grid grid = GridService.getInstance().getGrid(zone.number,"default");
			
			TrackData td = grid.get(playerId);
			if (td == null) {
				logger.warning("TrackData not found for "+playerId);
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
			StatusEffectManager.tell("default", zone.number, statusEffectTarget.clone(), getSelf());
		}
		
		if (sendToObjectGrid) {
			StatusEffectManager.tell("build_objects", zone.number, statusEffectTarget.clone(), getSelf());
		}
		
		sendAttack(attack,zone.number);
	}

}
