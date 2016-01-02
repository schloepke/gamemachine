package plugins.core;

import io.gamemachine.config.GridConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.regions.ZoneService;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CharacterLocationHandler extends GameMessageActor {

	public static class LocationUpdate {
		public int x;
		public int y;
		public int z;
		public String entityId;

		public LocationUpdate(String entityId, int x, int y, int z) {
			this.entityId = entityId;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private static Map<String, LocationUpdate> updates = new HashMap<String, LocationUpdate>();

	public static String name = CharacterLocationHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	private Long updateInterval = 2000l;
	private long saveInterval = 10000l;
	private long lastSave = 0l;

	@Override
	public void awake() {
		scheduleOnce(updateInterval, "update");

	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub

	}

	public void onTick(String message) {
		updateLocations();
		if (System.currentTimeMillis() - lastSave > saveInterval) {
			saveLocations();
		}
		scheduleOnce(updateInterval, "update");
	}

	private void updateLocations() {
		for (Grid grid : GridService.getInstance().getGrids()) {
			if (grid.getType() != GridConfig.GridType.Moving) {
				continue;
			}

			for (TrackData trackData : grid.getAll()) {
				if (trackData.entityType != TrackData.EntityType.Player) {
					continue;
				}

				String characterId = PlayerService.getInstance().getCharacterId(trackData.id);
				if (Strings.isNullOrEmpty(characterId)) {
					continue;
				}

				LocationUpdate update;

				if (updates.containsKey(characterId)) {
					update = updates.get(characterId);
					update.x = trackData.x;
					update.y = trackData.y;
					update.z = trackData.z;
				} else {
					update = new LocationUpdate(trackData.id, trackData.x, trackData.y, trackData.z);
					updates.put(characterId, update);
				}

			}
		}
	}

	private void saveLocations() {
		for (String key : updates.keySet()) {
			LocationUpdate update = updates.get(key);

			Character character = CharacterService.instance().find(update.entityId, key);
			if (character != null) {
				character.worldx = update.x;
				character.worldy = update.y;
				character.worldz = update.z;
				CharacterService.instance().save(character);
			}
		}
		lastSave = System.currentTimeMillis();
	}

}
