package plugins.combat;

import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Attack;
import io.gamemachine.messages.ComboAttack;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffectTarget;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;

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

		Grid grid = GameGrid.getGameGrid("mygame", "default", attack.attacker);
		for (TrackData trackData : grid.getAll()) {
			if (!attack.attacker.equals(trackData.id)) {
				msg.attack = attack;
				PlayerCommands.sendGameMessage(msg, trackData.id);
			}
		}
	}

		
	private void doAttack(Attack attack) {
		int zone = GameGrid.getPlayerZone(playerId);
		VitalsHandler vitalsHandler = VitalsHandler.getHandler("default", zone);
		
		logger.warning("Attack " + attack.attacker + " " + attack.target + " skill " + attack.skill);
		PlayerSkill skill = PlayerSkillHandler.globalPlayerSkills.get(attack.skill);
		StatusEffectTarget target = new StatusEffectTarget();
		target.location = attack.targetLocation;
		target.range = skill.range;
		target.skill = skill.id;
		target.origin = attack.attacker;
		target.originPlayerId = playerId;

		if (skill.skillType.equals(PlayerSkill.SkillType.Passive.toString())) {
			logger.warning("Set passive flags");
			target.action = StatusEffectTarget.Action.Apply;
			target.passiveFlag = StatusEffectTarget.PassiveFlag.AutoRemove;
		} else {
			target.action = StatusEffectTarget.Action.Apply;
			target.passiveFlag = StatusEffectTarget.PassiveFlag.NA;
		}

		if (skill.damageType.equals(PlayerSkill.DamageType.Aoe.toString())) {
			target.target = PlayerSkill.DamageType.Aoe.toString();
		} else if (skill.damageType.equals(PlayerSkill.DamageType.SelfAoe.toString())) {
			target.target = PlayerSkill.DamageType.SelfAoe.toString();
		} else if (skill.damageType.equals(PlayerSkill.DamageType.SingleTarget.toString())) {
			target.target = attack.target;
		} else if (skill.damageType.equals(PlayerSkill.DamageType.Self.toString())) {
			target.target = attack.target;
		}
		

		
		Vitals vitals = vitalsHandler.findOrCreate(target.origin);
		if (skill.resource.equals(PlayerSkill.Resource.Stamina.toString())) {
			if (vitals.stamina < skill.resourceCost) {
				logger.warning("Insufficient stamina needed " + skill.resourceCost);
				return;
			}
			vitals.stamina -= skill.resourceCost;
		} else if (skill.resource.equals(PlayerSkill.Resource.Magic.toString())) {
			if (vitals.magic < skill.resourceCost) {
				logger.warning("Insufficient magic needed " + skill.resourceCost);
				return;
			}
			vitals.magic -= skill.resourceCost;
		}

		
		StatusEffectHandler.tell("default",zone, target, getSelf());
		StatusEffectHandler.tell("build_objects",zone, target.clone(), getSelf());
		
		sendAttack(attack);
	}

}
