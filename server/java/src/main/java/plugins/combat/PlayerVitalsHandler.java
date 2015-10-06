package plugins.combat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import plugins.clientDbLoader.ClientDbLoader;

public class PlayerVitalsHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlayerVitalsHandler.class);
	public static ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();
	public static Map<Integer,Vitals> templates = new HashMap<Integer,Vitals>();
	
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
	
	private static void LoadTemplates() {
		VitalsContainer container = ClientDbLoader.getVitalsContainer();
		if (container != null) {
			for (Vitals vitals : ClientDbLoader.getVitalsContainer().vitals) {
				templates.put(vitals.type.number,vitals);
			}
		}
	}
	
	public static Vitals getOrCreate(String gridName, String characterId) {
		if (templates.size() == 0) {
			LoadTemplates();
		}
		
		if (characterId == null) {
			throw new RuntimeException("Character id is null!!");
		}
		
		if (!playerVitals.containsKey(characterId)) {
			
			Character character = CharacterService.getInstance().find(characterId);
			Player player = PlayerService.getInstance().findByCharacterId(characterId);
			if (character == null) {
				throw new RuntimeException("No character found with id "+characterId);
			}
			
			Vitals vitals = templates.get(character.vitalsType);
			if (vitals == null) {
				vitals = new Vitals();
			}
			
			vitals.playerId = player.id;
			vitals.grid = Common.gameGrid(player.id).name;
			
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(characterId);
	}
}
