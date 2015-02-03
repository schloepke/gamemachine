package pvp_game;

import java.util.List;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;

public class PlayerSkillHandler extends GameMessageActor {

	public static String name = PlayerSkillHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

		
	@Override
	public void awake() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.hasPlayerSkills()) {
				gameMessage.playerSkills.playerSkill = PlayerSkill.db().where("player_skill_character_id = ?", characterId);
				setReply(gameMessage);
			}
		}

	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub

	}

}
