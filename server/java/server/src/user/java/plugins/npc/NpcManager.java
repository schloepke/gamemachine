package plugins.npc;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.Vitals;
import io.gamemachine.unity.UnityMessageHandler;
import io.gamemachine.unity.unity_engine.RegionData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/3/2016.
 */
public class NpcManager extends GameMessageActor {

    public static String name = NpcManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private static Map<String,RegionData> regions = new ConcurrentHashMap<>();

    private long updateInterval = 5000L;
    private NpcService npcService;

    public NpcManager() {
        npcService = NpcService.instance();
    }

    @Override
    public void awake() {
        scheduleOnce(updateInterval, "updateRegions");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {

    }

    public void updateRegions() {
        for (RegionData regionData : UnityMessageHandler.getRegions()) {
            if (regionData.isAlive && regionData.status == RegionData.Status.Inactive) {
                setRegionActive(regionData);
            } else if (!regionData.isAlive && regionData.status == RegionData.Status.Active) {
                setRegionInactive(regionData);
            }
        }
    }

    private void setRegionInactive(RegionData regionData) {
        logger.warning("De-Activating region "+regionData.region);
    }

    private void setRegionActive(RegionData regionData) {
        logger.warning("Activating region "+regionData.region);

        int groupCount = (int) (regionData.size / 100);

        for(int i=0;i<groupCount;i++) {
            NpcGroup group = new NpcGroup();
            for (int j=0;j<4;j++) {

            }
        }
    }

}
