package pvp_game;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.Grid;
import io.gamemachine.messages.TrackData;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class NpcHandler extends UntypedActor {

	public static String name = NpcHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			Grid grid = GameGrid.getGameGrid("mygame", "default");
			for (Npc npc : Npc.npcs.values()) {
				TrackData trackData = new TrackData();
				trackData.setId(npc.name);
				trackData.setCharacterId(npc.characterId);
				trackData.setEntityType(io.gamemachine.messages.TrackData.EntityType.NPC);
				trackData.setX((int) Math.round((npc.position.x+Common.worldOffset) * 100));
				trackData.setY((int) Math.round((npc.position.y+Common.worldOffset) * 100));
				trackData.setZ((int) Math.round((npc.position.z+Common.worldOffset) * 100));
				grid.set(trackData);
			}
			tick(100L, "tick");
		}
		
	}
	
	@Override
	public void preStart() {
		tick(100L, "tick");
	}

	public void tick(long delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
}
