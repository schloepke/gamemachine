
package plugins.core.combat;

import java.util.HashMap;
import java.util.Map;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerMessage;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.SiegeCommand;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.Zone;
import plugins.landrush.BuildObjectHandler;

public class SiegeHandler extends GameMessageActor {

    public static String name = SiegeHandler.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public Map<String, String> sieges = new HashMap<String, String>();

    @Override
    public void awake() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {

        if (exactlyOnce(gameMessage)) {
            if (gameMessage.siegeCommand.action == SiegeCommand.Action.Use) {
                logger.warning("UseSiege");
                useSiege(gameMessage);
            } else if (gameMessage.siegeCommand.action == SiegeCommand.Action.Release) {
                logger.warning("ReleaseSiege");
                releaseSiege(gameMessage);
            }
        } else {
            if (gameMessage.siegeCommand.action == SiegeCommand.Action.SetRotation) {
                updateRotation(gameMessage);
            } else if (gameMessage.siegeCommand.action == SiegeCommand.Action.Fire) {
                logger.warning("FireSiege");
                fireSiege(gameMessage);
            }
        }
    }


    private boolean canUse(String buildObjectId, String ownerId) {
        if (!sieges.containsKey(buildObjectId) && ownerId.equals(characterId)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canRelease(String buildObjectId, String ownerId) {
        if (sieges.containsKey(buildObjectId) && sieges.get(buildObjectId).equals(characterId)) {
            return true;
        } else {
            return false;
        }
    }

    private void fireSiege(GameMessage gameMessage) {
        Zone zone = PlayerService.getInstance().getZone(playerId);
        BuildObject existing = BuildObjectHandler.find(gameMessage.siegeCommand.buildObjectId, zone.name);
        if (existing != null) {
            if (canRelease(existing.id, existing.ownerId)) {
                gameMessage.siegeCommand.result = SiegeCommand.Result.Approved;
                broadcast(gameMessage, existing.zone);
                logger.warning("FireSiege approved");
            }
        }
    }

    private void releaseSiege(GameMessage gameMessage) {
        Zone zone = PlayerService.getInstance().getZone(playerId);
        BuildObject existing = BuildObjectHandler.find(gameMessage.siegeCommand.buildObjectId, zone.name);
        if (existing != null) {
            if (canRelease(existing.id, existing.ownerId)) {
                sieges.remove(existing.id);
                gameMessage.siegeCommand.result = SiegeCommand.Result.Approved;
                setReply(gameMessage);
                broadcast(gameMessage, existing.zone);
                logger.warning("ReleaseSiege approved");
            }
        }
    }

    private void useSiege(GameMessage gameMessage) {
        Zone zone = PlayerService.getInstance().getZone(playerId);
        BuildObject existing = BuildObjectHandler.find(gameMessage.siegeCommand.buildObjectId, zone.name);
        if (existing != null) {
            if (canUse(existing.id, existing.ownerId) || canRelease(existing.id, existing.ownerId)) {
                sieges.put(existing.id, characterId);
                gameMessage.siegeCommand.result = SiegeCommand.Result.Approved;
                setReply(gameMessage);
                broadcast(gameMessage, existing.zone);
                logger.warning("UseSiege approved");
            }
        }
    }

    private void updateRotation(GameMessage gameMessage) {
        BuildObject existing = BuildObjectHandler.updateRotation(gameMessage.siegeCommand.buildObject, gameMessage.playerId);
        if (existing != null) {
            //gameMessage.siegeCommand.buildObject = existing;
            gameMessage.siegeCommand.result = SiegeCommand.Result.Approved;
            broadcast(gameMessage, existing.zone);
            logger.warning("broadcast sent");
        }
    }

    private void broadcast(GameMessage gameMessage, String zone) {
        Grid grid = GridService.getInstance().getGrid(zone, "default");
        for (TrackData trackData : grid.getAll()) {
            if (trackData.entityType != TrackData.EntityType.Player) {
                continue;
            }
            PlayerMessage.tell(gameMessage.clone(), trackData.id);
        }
    }

}
