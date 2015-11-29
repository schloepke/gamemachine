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

import com.google.common.base.Strings;

import akka.event.Logging;
import akka.event.LoggingAdapter;


public class CharacterLocationHandler extends GameMessageActor  {

	public static String name = CharacterLocationHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	private Long updateInterval = 10000l;
	
	@Override
	public void awake() {
		scheduleOnce(updateInterval,"update");
		
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}

	public void onTick(String message) {
		updateLocations();
		scheduleOnce(updateInterval,"update");
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
				
				Character character  = CharacterService.instance().find(trackData.id, characterId);
				if (character != null) {
					character.worldx = trackData.x;
					character.worldy = trackData.y;
					character.worldz = trackData.z;
					character.zone = ZoneService.getZone(trackData.zone);
					CharacterService.instance().save(character);
				}
			}
		}
	}
	
}
