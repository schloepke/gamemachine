package plugins.combat;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.CharacterService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.PlayerSkill;
import io.gamemachine.messages.StatusEffect;
import plugins.core.combat.PlayerSkillHandler;

public class CombatDamage {

	private static final Logger logger = LoggerFactory.getLogger(CombatDamage.class);
	
	public int getEffectValue(StatusEffect statusEffect, String playerSkillId, String characterId) {
		int base = randInt(statusEffect.minValue, statusEffect.maxValue);
		Character character = CharacterService.instance().find(characterId);
		float level = skillLevel(playerSkillId, character.id);
		return base * (int)level * effectiveCharacterLevel(character);
	}
	
	private int effectiveCharacterLevel(Character character) {
		if (character.level > 0) {
			return character.level;
		} else {
			return 1;
		}
	}
	
	private int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	public void skillUsed(String playerSkillId, String characterId) {
		int chance = randInt(1, 20);
		if (chance > 1) {
			return;
		}

		Character character = CharacterService.instance().find(characterId);
		if (character == null) {
			logger.warn("Unable to find character for " + characterId);
			return;
		}

		PlayerSkill skill = PlayerSkillHandler.findSkill(playerSkillId, character.id, true);
		skill.level += (new Random().nextFloat() + 0.01f) / 10f;
		PlayerSkillHandler.saveSkill(skill,character.id);
		logger.warn("Skill up " + playerSkillId + " level " + skill.level);
		
	}
	private float skillLevel(String id, String characterId) {
		PlayerSkill skill = PlayerSkillHandler.findSkill(id, characterId, true);
		return skill.level;
	}
}
