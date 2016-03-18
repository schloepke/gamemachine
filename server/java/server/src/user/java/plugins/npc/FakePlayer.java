package plugins.npc;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.messages.TrackData;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

/**
 * Created by chris on 3/10/2016.
 */
public class FakePlayer extends NpcBase {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);



    public FakePlayer(String characterId, String region) {
        super(characterId,region);

        position = new Vector3(10d,1d,10d);
    }

    protected void doStarting() {
        trackData.entityType = TrackData.EntityType.Player;
        setState(State.Running);
    }

    protected void doIdle() {

    }


    protected void doRunning() {

        lastUpdate = System.currentTimeMillis();
        sendTrackData();

    }
}
