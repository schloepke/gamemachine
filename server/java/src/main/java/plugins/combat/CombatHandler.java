package plugins.combat;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Character;

public class CombatHandler extends GameMessageActor {

	public static String name = CombatHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);


	@Override
	public void awake() {
		scheduleOnce(5000L,"vitals");
	}

	@Override
	public void onTick(String message) {
		VitalsHandler.UpdateVitals();
		scheduleOnce(5000L,"vitals");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (gameMessage.attack != null) {
			doAttack(gameMessage.attack);
		} else if (gameMessage.dataRequest != null) {
			//effectHandler.tell(gameMessage.dataRequest, getSelf());
		}
	}

	private void sendAttack(Attack attack) {
		GameMessage msg = new GameMessage();

		Grid grid = GameGrid.getGameGrid(AppConfig.getDefaultGameId(), "default", playerId);
		for (TrackData trackData : grid.getAll()) {
			if (!playerId.equals(trackData.id)) {
				msg.attack = attack;
				PlayerCommands.sendGameMessage(msg, trackData.id);
			}
		}
	}

		
	private void doAttack(Attack attack) {
		int zone = GameGrid.getEntityZone(playerId);
        
		logger.warning("Attack " + attack.attackerCharacterId + " " + attack.targetId + " skill " + attack.playerSkill.id);
		StatusEffectTarget statusEffectTarget = new StatusEffectTarget();
		
		Character character = CharacterService.instance().find(attack.targetId);
        if (character == null) {
        	statusEffectTarget.targetEntityId = attack.targetId;
        } else {
        	statusEffectTarget.targetEntityId = character.playerId;
        }
        
		statusEffectTarget.location = attack.targetLocation;
		statusEffectTarget.originCharacterId = attack.attackerCharacterId;
		statusEffectTarget.originEntityId = playerId;

		if (attack.playerSkill.skillType.equals(PlayerSkill.SkillType.Passive.toString())) {
			logger.warning("Set passive flags");
			statusEffectTarget.action = StatusEffectTarget.Action.Apply;
			statusEffectTarget.passiveFlag = StatusEffectTarget.PassiveFlag.AutoRemove;
		} else {
			statusEffectTarget.action = StatusEffectTarget.Action.Apply;
			statusEffectTarget.passiveFlag = StatusEffectTarget.PassiveFlag.NA;
		}
		statusEffectTarget.attack = attack;
				
		StatusEffectHandler.tell("default",zone, statusEffectTarget.clone(), getSelf());
		StatusEffectHandler.tell("build_objects",zone, statusEffectTarget.clone(), getSelf());
		
		sendAttack(attack);
	}

}
