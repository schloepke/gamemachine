package plugins.core.combat;

import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.PlayerSkills;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.StatusEffects;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import java.util.List;

public class StatusEffectData {

    private static final Logger logger = LoggerFactory.getLogger(StatusEffectData.class);
    public static ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();
    public static ConcurrentHashMap<String, List<StatusEffect>> skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

    public static void createStatusEffects() {
        statusEffects = new ConcurrentHashMap<String, StatusEffect>();
        skillEffects = new ConcurrentHashMap<String, List<StatusEffect>>();

        StatusEffects effects = ClientDbLoader.getStatusEffects();
        PlayerSkills playerSkills = ClientDbLoader.getPlayerSkills();
        if (effects != null && playerSkills != null) {
            for (PlayerSkill playerSkill : playerSkills.playerSkill) {
                if (!Strings.isNullOrEmpty(playerSkill.statusEffects)) {
                    for (String effectId : playerSkill.statusEffects.split(",")) {
                        for (StatusEffect effect : effects.statusEffect) {
                            if (effect.id.equals(effectId)) {
                                addSkill(playerSkill.id, effect);
                            }
                        }
                    }
                }
            }
        } else {
            logger.warn("Unable to load effects/player skils catalogs");
        }
    }


    private static void addSkill(String skill, StatusEffect e) {
        if (!skillEffects.containsKey(skill)) {
            skillEffects.put(skill, new ArrayList<StatusEffect>());
        }
        skillEffects.get(skill).add(e);
        statusEffects.put(e.id, e);
    }
}
