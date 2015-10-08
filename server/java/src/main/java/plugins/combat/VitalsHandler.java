package plugins.combat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static List<Vitals> templates;
	private static final Logger logger = LoggerFactory.getLogger(VitalsHandler.class);
	private static ConcurrentHashMap<String, Vitals> entityVitals = new ConcurrentHashMap<String, Vitals>();

	public static void loadTemplates() {
		VitalsContainer container = ClientDbLoader.getVitalsContainer();
		if (container != null) {
			templates = container.vitals;
		}
	}

	public static Vitals fromTrackData(TrackData trackData, int zone) {
		if (trackData.entityType == TrackData.EntityType.Object) {
			return findOrCreateObjectVitals(trackData.id, Vitals.VitalsType.Object.number, zone);
		} else if (trackData.entityType == TrackData.EntityType.Player
				|| trackData.entityType == TrackData.EntityType.Npc) {
			Player player = PlayerService.getInstance().find(trackData.id);
			return findOrCreateCharacterVitals(player.characterId);
		} else {
			throw new RuntimeException("Can't get vitals for entity type " + trackData.entityType.toString());
		}
	}

	public static Vitals getTemplate(Vitals vitals) {
		return getTemplate(vitals.type.number, vitals.subType.number);
	}

	public static Vitals getTemplate(int vitalsType, int vitalsSubType) {
		for (Vitals template : templates) {
			if (template.type.number == vitalsType && template.subType.number == vitalsSubType) {
				return template.clone();
			}
		}
		
		Vitals vitals = new Vitals();
		setAttribute(vitals,Vitals.Attribute.Health,1000);
		setAttribute(vitals,Vitals.Attribute.Stamina,1000);
		setAttribute(vitals,Vitals.Attribute.Magic,1000);
		
		vitals.type = Vitals.VitalsType.valueOf(vitalsType);
		vitals.subType = Vitals.SubType.valueOf(vitalsSubType);
		return vitals;
	}


	public static void remove(String id) {
		if (entityVitals.containsKey(id)) {
			entityVitals.remove(id);
		}
	}

	public static Collection<Vitals> getVitals() {
		return entityVitals.values();
	}

	public static Vitals findOrCreateObjectVitals(String entityId, int vitalsType, int zone) {
		if (entityId == null) {
			throw new RuntimeException("Object id is null!!");
		}

		if (!entityVitals.containsKey(entityId)) {
			Vitals vitals = getTemplate(vitalsType, Vitals.SubType.Structure.number);
			vitals.zone = zone;
			vitals.entityId = entityId;
			entityVitals.put(vitals.entityId, vitals);
		}
		return entityVitals.get(entityId);
	}

	public static Vitals findOrCreateCharacterVitals(String entityId) {

		if (entityId == null) {
			throw new RuntimeException("Entity id is null!!");
		}

		if (!entityVitals.containsKey(entityId)) {
			
			Player player = PlayerService.getInstance().find(entityId);
			if (player == null) {
				throw new RuntimeException("No player found with id " + entityId);
			}
			Character character = CharacterService.instance().find(player.characterId);
			Vitals vitals = getTemplate(character.vitalsType, character.vitalsSubType);

			vitals.characterId = character.id;
			vitals.entityId = player.id;
			vitals.zone = GameGrid.getEntityZone(player.id);
			entityVitals.put(vitals.entityId, vitals);
		}
		return entityVitals.get(entityId);
	}
	
	public static int getAttribute(Vitals vitals, Vitals.Attribute attribute) {
		String name = attribute.toString();
		name = java.lang.Character.toLowerCase(name.charAt(0)) + name.substring(1);
		try {
			Field field = Vitals.class.getField(name);
			return (int)field.getInt(vitals);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void setAttribute(Vitals vitals, Vitals.Attribute attribute, int value) {
		String name = attribute.toString();
		name = java.lang.Character.toLowerCase(name.charAt(0)) + name.substring(1);
		try {
			Field field = Vitals.class.getField(name);
			field.setInt(vitals, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
