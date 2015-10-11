package plugins.combat;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerSkill;

public class PlayerSkillHandler extends GameMessageActor {

	public static String name = PlayerSkillHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static ConcurrentHashMap<String, PlayerSkill> globalPlayerSkills = new ConcurrentHashMap<String, PlayerSkill>();
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerSkill>> playerSkills = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerSkill>>();

		
	@Override
	public void awake() {
		List<PlayerSkill> skills = ClientDbLoader.getPlayerSkills().getPlayerSkillList();
		for (PlayerSkill playerSkill : skills) {
			globalPlayerSkills.put(playerSkill.id, playerSkill);
			logger.warning("Loading skill "+playerSkill.id);
		}
	}

	public static boolean hasPlayerSkill(String id, String characterId) {
		if (playerSkills.containsKey(characterId)) {
			if (playerSkills.get(characterId).containsKey(id)) {
				return true;
			}
		}
		return false;
	}
	
	public static PlayerSkill playerSkill(String id, String characterId) {
		if (!playerSkills.containsKey(characterId)) {
			throw new RuntimeException("cannot find player skills for "+characterId);
		}
		ConcurrentHashMap<String, PlayerSkill> skills = playerSkills.get(characterId);
		return skills.get(id);
	}
	
	public static void savePlayerSkill(PlayerSkill playerSkill) {
		if (!playerSkills.containsKey(playerSkill.characterId)) {
			throw new RuntimeException("cannot find player skills for "+playerSkill.characterId);
		}
		ConcurrentHashMap<String, PlayerSkill> skills = playerSkills.get(playerSkill.characterId);
		skills.put(playerSkill.id, playerSkill);
		PlayerSkill.db().save(playerSkill);
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.playerSkills != null) {
				gameMessage.playerSkills.playerSkill = PlayerSkill.db().where("player_skill_character_id = ?", characterId);
				setReply(gameMessage);
			}
		}

	}

}
