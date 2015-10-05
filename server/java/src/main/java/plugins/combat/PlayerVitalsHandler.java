package plugins.combat;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Vitals;
import io.gamemachine.net.http.HttpServerHandler;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerVitalsHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlayerVitalsHandler.class);
	public static ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();
	
	public static Vitals get(String id) {
		return playerVitals.get(id);
	}
	
	public static void remove(String id) {
		if (playerVitals.containsKey(id)) {
			playerVitals.remove(id);
		}
	}
	
	public static Collection<Vitals> getVitals() {
		return playerVitals.values();
	}
	
	public static Vitals getOrCreate(String gridName, String id) {
		if (!playerVitals.containsKey(id)) {
			Vitals vitals = new Vitals();
			Character character = CharacterService.getInstance().find(id);
			if (character == null) {
				logger.error("No character found with id "+id);
				return null;
			}
			
			vitals.changed = 1;
			vitals.id = id;
			vitals.dead = 0;
			vitals.health = CharacterService.getInstance().find(id).health;
			vitals.stamina = CharacterService.getInstance().find(id).stamina;
			vitals.magic = CharacterService.getInstance().find(id).magic;
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
