package plugins.combat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import plugins.clientDbLoader.ClientDbLoader;

public class VitalsHandler {

	private static Map<Integer,Vitals> templates = new HashMap<Integer,Vitals>();
	private static final Logger logger = LoggerFactory.getLogger(VitalsHandler.class);
	public ConcurrentHashMap<String, Vitals> playerVitals = new ConcurrentHashMap<String, Vitals>();
	public static ConcurrentHashMap<String, VitalsHandler> handlers = new ConcurrentHashMap<String, VitalsHandler>();

	public static void loadTemplates() {
		VitalsContainer container = ClientDbLoader.getVitalsContainer();
		if (container != null) {
			for (Vitals vitals : ClientDbLoader.getVitalsContainer().vitals) {
				templates.put(vitals.type.number,vitals);
			}
		}
	}
	
	public Vitals fromTrackData(TrackData trackData, int zone) {
		if (trackData.entityType == TrackData.EntityType.Object) {
			return findOrCreateObject(trackData.id,Vitals.VitalsType.Object.number,zone);
		} else if (trackData.entityType == TrackData.EntityType.Player || trackData.entityType == TrackData.EntityType.Npc){
			Player player = PlayerService.getInstance().find(trackData.id);
			return findOrCreate(player.characterId);
		} else {
			throw new RuntimeException("Can't get vitals for entity type "+trackData.entityType.toString());
		}
	}
		
	public static Vitals getTemplate(Vitals vitals) {
		if (!templates.containsKey(vitals.type.number)) {
			throw new RuntimeException("Vitals template not found "+vitals.type.number);
		}
		return templates.get(vitals.type.number).clone();
	}
	
	public static Vitals getObjectTemplate(String id, int vitalsType) {
		Vitals vitals = null;
		if (templates.containsKey(vitalsType)) {
			vitals = templates.get(vitalsType).clone();
		} else {
			vitals = new Vitals();
			vitals.health = 1000;
			vitals.stamina = 1000;
			vitals.magic = 1000;
			vitals.type = Vitals.VitalsType.valueOf(vitalsType);
		}
		
		vitals.id = id;
		return vitals;
	}
	
	public static Vitals getCharacterTemplate(String characterId) {
		Character character = CharacterService.instance().find(characterId);
		if (character == null) {
			throw new RuntimeException("Unable to find character "+characterId);
		}
		return getCharacterTemplate(character);
	}
	
	public static Vitals getCharacterTemplate(Character character) {
		Vitals vitals = null;
		if (templates.containsKey(character.vitalsType)) {
			vitals = templates.get(character.vitalsType).clone();
		} else {
			vitals = new Vitals();
			vitals.health = 1000;
			vitals.stamina = 1000;
			vitals.magic = 1000;
			vitals.type = Vitals.VitalsType.valueOf(character.vitalsType);
			if (vitals.type == null) {
				throw new RuntimeException("No vitalsType for "+character.vitalsType);
			}
		}
		
		vitals.id = character.id;
		return vitals;
	}
	
	public static Map<Integer,Vitals> getTemplates() {
		return templates;
	}
	
	public static VitalsHandler getHandler(String gridName, int zone) {
		String key = gridName + zone;
		if (!handlers.containsKey(key)) {
			VitalsHandler handler = new VitalsHandler();
			handlers.put(key, handler);
		}
		return handlers.get(key);
	}

	public static void UpdateVitals() {
		for (VitalsHandler handler : handlers.values()) {
			for (Vitals vitals : handler.playerVitals.values()) {
				if (!Strings.isNullOrEmpty(vitals.playerId)) {
					int zone = GameGrid.getPlayerZone(vitals.playerId);
					if (zone != vitals.zone) {
						handler.remove(vitals.id);
					}
				}
			}
		}
	}

	public void remove(String id) {
		if (playerVitals.containsKey(id)) {
			playerVitals.remove(id);
		}
	}

	public Collection<Vitals> getVitals() {
		return playerVitals.values();
	}

	public Vitals findOrCreateObject(String objectId, int vitalsType, int zone) {
		if (objectId == null) {
			throw new RuntimeException("Object id is null!!");
		}

		if (!playerVitals.containsKey(objectId)) {
			Vitals vitals = getObjectTemplate(objectId, vitalsType);
			vitals.zone = zone;
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(objectId);
	}

	public Vitals findOrCreate(String characterId) {

		if (characterId == null) {
			throw new RuntimeException("Character id is null!!");
		}

		if (!playerVitals.containsKey(characterId)) {
			Character character = CharacterService.instance().find(characterId);
			Player player = PlayerService.getInstance().findByCharacterId(characterId);
			if (character == null) {
				throw new RuntimeException("No character found with id " + characterId);
			}

			Vitals vitals = getCharacterTemplate(character);
			
			vitals.playerId = player.id;
			vitals.zone = GameGrid.getPlayerZone(player.id);
			playerVitals.put(vitals.id, vitals);
		}
		return playerVitals.get(characterId);
	}
}
