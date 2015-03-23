package io.gamemachine.core;

import io.gamemachine.messages.Vitals;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import pvp_game.CharacterHandler;

public class PlayerVitalsHandler {

	public static ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();
	
	public static Vitals get(String id) {
		return playerVitals.get(id);
	}
	
	public static Collection<Vitals> getVitals() {
		return playerVitals.values();
	}
	
	public static Vitals getOrCreate(String gridName, String id) {
		if (!playerVitals.containsKey(id)) {
			Vitals vitals = new Vitals();

			vitals.changed = 1;
			vitals.id = id;
			vitals.dead = 0;
			vitals.health = CharacterHandler.currentHealth(id);
			vitals.stamina = CharacterHandler.currentStamina(id);
			vitals.magic = CharacterHandler.currentMagic(id);
			vitals.armor = 0;
			vitals.elementalResist = 0;
			vitals.spellResist = 0;
			vitals.spellPenetration = 0;
			vitals.magicRegen = 0;
			vitals.healthRegen = 0;
			vitals.staminaRegen = 0;
			vitals.lastCombat = 0l;
			vitals.grid = GameGrid.getPlayerGrid(gridName, id).name;
			
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(id);
	}
}
