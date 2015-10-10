package io.gamemachine.core;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.Character;

public interface GameEntityManager {

	Vitals getBaseVitals(String characterId);
	Vitals getBaseVitals(String entityId, Vitals.VitalsType vitalsType);
	void OnCharacterCreated(Character character, Object data);
}
