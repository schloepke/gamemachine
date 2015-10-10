package plugins.combat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.Commands;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Vitals;
import io.gamemachine.messages.VitalsContainer;
import plugins.clientDbLoader.ClientDbLoader;

public class VitalsHandler extends GameMessageActor {

	public static String name = VitalsHandler.class.getSimpleName();
	private static List<Vitals> baseVitals;
	private static final Logger logger = LoggerFactory.getLogger(VitalsHandler.class);
	private static ConcurrentHashMap<String, Vitals> entityVitals = new ConcurrentHashMap<String, Vitals>();
	private static ConcurrentHashMap<Integer, Field> fields = new ConcurrentHashMap<Integer, Field>();
	
	public static void loadTemplates() {
		VitalsContainer container = ClientDbLoader.getVitalsContainer();
		if (container != null) {
			baseVitals = container.vitals;
		}
	}

	public static Vitals fromTrackData(TrackData trackData, int zone) {
		if (trackData.entityType == TrackData.EntityType.Object) {
			return findOrCreateObjectVitals(trackData.id, Vitals.VitalsType.Object.number, zone);
		} else if (trackData.entityType == TrackData.EntityType.Player
				|| trackData.entityType == TrackData.EntityType.Npc) {
			return findOrCreateCharacterVitals(trackData.id);
		} else {
			throw new RuntimeException("Can't get vitals for entity type " + trackData.entityType.toString());
		}
	}

	public static Vitals getBase(Vitals vitals) {
		return getBase(vitals.type.number, vitals.subType.number);
	}

	public static Vitals getBase(int vitalsType, int vitalsSubType) {
		for (Vitals template : baseVitals) {
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

	public static List<Vitals> getVitalsForZone(int zone) {
		List<Vitals> zoneVitals = new ArrayList<Vitals>();
		for (Vitals vitals : entityVitals.values()) {
			if (vitals.zone == zone) {
				zoneVitals.add(vitals);
			}
		}
		return zoneVitals;
	}
	
	public static Collection<Vitals> getVitals() {
		return entityVitals.values();
	}

	public static Vitals findOrCreateObjectVitals(String entityId, int vitalsType, int zone) {
		if (entityId == null) {
			throw new RuntimeException("Object id is null!!");
		}

		if (!entityVitals.containsKey(entityId)) {
			Vitals vitals = getBase(vitalsType, Vitals.SubType.Structure.number);
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
			if (Strings.isNullOrEmpty(player.characterId)) {
				throw new RuntimeException("Player character id is null ");
			}
			
			Character character = CharacterService.instance().find(player.characterId);
			Vitals vitals = getBase(character.vitalsType, character.vitalsSubType);

			vitals.characterId = character.id;
			vitals.entityId = player.id;
			vitals.zone = GameGrid.getEntityZone(player.id);
			entityVitals.put(vitals.entityId, vitals);
		}
		return entityVitals.get(entityId);
	}
	
	private static Field getField(Vitals.Attribute attribute) throws NoSuchFieldException, SecurityException {
		if (fields.containsKey(attribute.number)) {
			return fields.get(attribute.number);
		} else {
			String name = attribute.toString();
			name = java.lang.Character.toLowerCase(name.charAt(0)) + name.substring(1);
			Field field = Vitals.class.getField(name);
			fields.put(attribute.number, field);
			return field;
		}
	}
	
	public static int getAttribute(Vitals vitals, Vitals.Attribute attribute) {
		try {
			Field field = getField(attribute);
			return (int)field.getInt(vitals);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			logger.warn("getAttribute error "+attribute.toString()+" "+e.getMessage()+" returning 0");
			return 0;
		}
	}
	
	public static void setAttribute(Vitals vitals, Vitals.Attribute attribute, int value) {
		try {
			Field field = getField(attribute);
			field.setInt(vitals, value);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			logger.warn("setAttribute error "+attribute.toString()+" "+e.getMessage()+" no value set");
		}
	}

	@Override
	public void awake() {
		Commands.clientManagerRegister(name);
		scheduleOnce(5000L,"vitals");
	}

	@Override
	public void onTick(String message) {
		scheduleOnce(5000L,"vitals");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}
	
	public void onPlayerDisconnect(String playerId) {
		logger.warn("PlayerDisconnect "+playerId);
		VitalsHandler.remove(playerId);
	}
}
