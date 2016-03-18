package plugins.npc;


import akka.event.Logging;
import akka.event.LoggingAdapter;
import plugins.npc.NpcBase;

public class LandrushNpc extends SyncedNpc {

    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);


    public LandrushNpc(String characterId, String region) {
        super(characterId,region);

    }

    public void awake() {
    }
}
