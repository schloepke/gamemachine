package plugins.combat;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Vitals;

public class PlayerVitalsHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlayerVitalsHandler.class);
	public static ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();
	public static ConcurrentHashMap<String,PlayerVitalsHandler> handlers = new  ConcurrentHashMap<String,PlayerVitalsHandler>();
	
	public static PlayerVitalsHandler getHandler(String gridName,int zone) {
		String key = gridName+zone;
		if (!handlers.containsKey(key)) {
			PlayerVitalsHandler handler = new PlayerVitalsHandler();
			handlers.put(key, handler);
		}
		return handlers.get(key);
	}
	
	
	public  void remove(String id) {
		if (playerVitals.containsKey(id)) {
			playerVitals.remove(id);
		}
	}
	
	public Collection<Vitals> getVitals() {
		return playerVitals.values();
	}
	
		
	public Vitals findOrCreate(String characterId) {
		
		if (characterId == null) {
			throw new RuntimeException("Character id is null!!");
		}
		
		if (!playerVitals.containsKey(characterId)) {
			Character character = CharacterService.getInstance().find(characterId);
			Player player = PlayerService.getInstance().findByCharacterId(characterId);
			if (character == null) {
				throw new RuntimeException("No character found with id "+characterId);
			}
			
			Vitals vitals = CharacterService.getInstance().getVitalsTemplate(character);
			
			vitals.playerId = player.id;
			
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(characterId);
	}
}
