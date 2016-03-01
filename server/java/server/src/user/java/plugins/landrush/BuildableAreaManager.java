package plugins.landrush;

import java.util.List;

import com.google.common.collect.Lists;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.messages.BuildableArea;
import io.gamemachine.messages.BuildableAreas;
import io.gamemachine.messages.GameMessage;
import plugins.core.combat.ClientDbLoader;

public class BuildableAreaManager extends GameMessageActor {

    public static String name = BuildableAreaManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void awake() {
        register();
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {

        if (exactlyOnce(gameMessage)) {
            BuildableAreas areas = gameMessage.buildableAreas;
            if (areas.action == BuildableAreas.Action.GetAll) {

            } else if (areas.action == BuildableAreas.Action.Claim) {
                BuildableArea area = areas.getBuildableArea(0);
                claim(area);

            } else if (areas.action == BuildableAreas.Action.Release) {
                BuildableArea area = areas.getBuildableArea(0);
                release(area);
            }

            gameMessage.buildableAreas = new BuildableAreas();
            setReply(gameMessage);

            GameMessage msg = gameMessage.clone();
            msg.messageId = null;

            List<BuildableArea> buildableAreas = BuildableArea.db().findAll();
            List<List<BuildableArea>> subsets = Lists.partition(buildableAreas, 20);
            for (List<BuildableArea> subset : subsets) {
                for (BuildableArea buildableArea : subset) {
                    buildableArea.position = null;
                    buildableArea.size = null;
                }
                msg.buildableAreas.buildableArea = subset;
                PlayerMessage.tell(msg.clone(), playerId);
            }

        }
    }

    private void claim(BuildableArea area) {
        BuildableArea existing = find(area.id);
        if (existing != null) {
            existing.ownerId = area.ownerId;
            BuildableArea.db().save(existing);
            logger.warning("Area " + existing.ownerId + " claimed by " + playerId);
        }
    }

    private void release(BuildableArea area) {
        BuildableArea existing = find(area.id);
        if (existing != null) {
            existing.ownerId = "system";
            BuildableArea.db().save(existing);
            logger.warning("Area " + existing.ownerId + " released by " + playerId);
        }
    }


    private void register() {
        for (BuildableArea area : BuildableArea.db().findAll()) {
            if (area.ownerId.equals("system")) {
                BuildableArea.db().delete(area.recordId);
            }
        }

        BuildableAreas areas = ClientDbLoader.getBuildableAreas();
        int registered = 0;
        for (BuildableArea area : areas.getBuildableAreaList()) {
            BuildableArea existing = find(area.id);
            if (existing == null) {
                area.ownerId = "system";
                BuildableArea.db().save(area);
                registered++;
            }
        }
        logger.warning("Registered " + registered + " buildable areas");
    }

    public static BuildableArea find(String id) {
        return BuildableArea.db().findFirst("buildable_area_id = ?", id);

    }

}
