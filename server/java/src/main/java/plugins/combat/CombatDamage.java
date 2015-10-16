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
	
	public int getEffectValue(StatusEffect statusEffect, PlayerSkill playerSkill, String characterId) {
		int base = randInt(statusEffect.minValue, statusEffect.maxValue);
		Character character = CharacterService.instance().find(characterId);
		int level = skillLevel(playerSkill.id, character.id);
		return base * level * effectiveCharacterLevel(character);
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
	
	public void skillUsed(PlayerSkill playerSkill, String characterId) {
		int chance = randInt(1, 10);
		if (chance < 5) {
			return;
		}

		Character character = CharacterService.instance().find(characterId);
		if (character == null) {
			logger.warn("Unable to find character for " + characterId);
			return;
		}

		if (PlayerSkillHandler.hasPlayerSkill(playerSkill.id, character.id)) {
			PlayerSkill existing = PlayerSkillHandler.playerSkill(playerSkill.id, character.id);
			existing.level += 1;
			PlayerSkillHandler.savePlayerSkill(existing);
			logger.warn("Skill up " + playerSkill.id + " level " + existing.level);
		}
	}
	// Can be a skill or player item
	private int skillLevel(String id, String characterId) {
		if (PlayerSkillHandler.hasPlayerSkill(id, characterId)) {
			PlayerSkill playerSkill = PlayerSkillHandler.playerSkill(id, characterId);
			return playerSkill.level;
		} else {
			return 1;
		}
	}
}
