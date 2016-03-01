package plugins.core.combat;

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

    private static ConcurrentHashMap<String, PlayerSkill> globalPlayerSkills = new ConcurrentHashMap<String, PlayerSkill>();
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerSkill>> playerSkills = new ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerSkill>>();


    @Override
    public void awake() {
        List<PlayerSkill> skills = ClientDbLoader.getPlayerSkills().getPlayerSkillList();
        for (PlayerSkill playerSkill : skills) {
            globalPlayerSkills.put(playerSkill.id, playerSkill);
            //logger.warning("Loading skill "+playerSkill.id);
        }
    }

    public static PlayerSkill getTemplate(String id) {
        return globalPlayerSkills.get(id).clone();
    }

    public static boolean hasSkill(String id, String characterId) {
        if (playerSkills.containsKey(characterId)) {
            if (playerSkills.get(characterId).containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public static PlayerSkill findSkill(String id, String characterId, boolean createIfNotExist) {
        if (!playerSkills.containsKey(characterId)) {
            playerSkills.put(characterId, new ConcurrentHashMap<String, PlayerSkill>());
        }
        ConcurrentHashMap<String, PlayerSkill> skills = playerSkills.get(characterId);

        PlayerSkill skill = null;
        if (skills.containsKey(id)) {
            return skills.get(id);
        } else if (createIfNotExist) {
            skill = getTemplate(id);
            skill.level = 1;
            saveSkill(skill, characterId);
            return skill;
        } else {
            return null;
        }
    }

    public static void saveSkill(PlayerSkill playerSkill, String characterId) {
        if (!playerSkills.containsKey(characterId)) {
            playerSkills.put(characterId, new ConcurrentHashMap<String, PlayerSkill>());
        }

        playerSkill.characterId = characterId;
        ConcurrentHashMap<String, PlayerSkill> skills = playerSkills.get(characterId);
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
