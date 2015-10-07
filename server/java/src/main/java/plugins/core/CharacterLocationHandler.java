package plugins.core;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
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
		for (Grid grid : GameGrid.gridsStartingWith("default")) {
			for (TrackData trackData : grid.getAll()) {
				if (trackData.entityType != TrackData.EntityType.Player) {
					continue;
				}
				String characterId = PlayerService.getInstance().getCharacter(trackData.id);
				Character character  = CharacterService.instance().find(trackData.id, characterId);
				if (character != null) {
					character.worldx = trackData.x;
					character.worldy = trackData.y;
					character.worldz = trackData.z;
					character.zone = trackData.zone;
					CharacterService.instance().save(character);
				}
			}
		}
	}
	
}
