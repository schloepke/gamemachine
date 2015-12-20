package io.gamemachine.core;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.ItemSlots;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.Vitals;

public interface GameEntityManager {

	Vitals getBaseVitals(String characterId);
	Vitals getBaseVitals(String entityId, Vitals.VitalsType vitalsType);
	void OnCharacterCreated(Character character, Object data);
	void OnPlayerConnected(String playerId);
	void OnPlayerDisConnected(String playerId);
	int getEffectValue(StatusEffect statusEffect, String playerSkillId, String characterId);
	void skillUsed(String playerSkillId, String characterId);
	void ItemSlotsUpdated(String playerId, String characterId, ItemSlots itemSlots);
}
