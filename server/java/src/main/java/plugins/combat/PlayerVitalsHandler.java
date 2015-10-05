package plugins.combat;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
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
	
	public static Vitals getOrCreate(String gridName, String characterId) {
		if (!playerVitals.containsKey(characterId)) {
			Vitals vitals = new Vitals();
			Character character = CharacterService.getInstance().find(characterId);
			Player player = PlayerService.getInstance().findByCharacterId(characterId);
			if (character == null) {
				logger.error("No character found with id "+characterId);
				return null;
			}
			
			vitals.changed = 1;
			vitals.id = characterId;
			vitals.dead = 0;
			vitals.health = character.health;
			vitals.stamina = character.stamina;
			vitals.magic = character.magic;
			vitals.armor = 0;
			vitals.elementalResist = 0;
			vitals.spellResist = 0;
			vitals.spellPenetration = 0;
			vitals.magicRegen = 0;
			vitals.healthRegen = 0;
			vitals.staminaRegen = 0;
			vitals.lastCombat = 0l;
			vitals.playerId = player.id;
			vitals.grid = Common.gameGrid(player.id).name;
			
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(characterId);
	}
}
