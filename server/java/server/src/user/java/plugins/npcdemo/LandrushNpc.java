package plugins.npcdemo;


import akka.event.Logging;
import akka.event.LoggingAdapter;
import plugins.npc.NpcBase;

public class LandrushNpc extends NpcBase {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public LandrushNpc(String playerId, String characterId, String region) {
        super(playerId, characterId,region);

    }

    public void awake() {

    }
}
