package plugins.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.Boat;
import io.gamemachine.messages.BoatAction;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.PlayerItem;
import io.gamemachine.messages.TrackData;
import plugins.inventoryservice.InventoryService;
import plugins.landrush.BuildObjectHandler;

public class BoatManager extends GameMessageActor {

    public static String name = BoatManager.class.getSimpleName();
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public static Map<String, Boat> boats = new ConcurrentHashMap<String, Boat>();

    private Long updateInterval = 1000l;
    private Long saveInterval = 20000l;

    public static Boat getBoat(String id) {
        if (boats.containsKey(id)) {
            return boats.get(id);
        } else {
            Boat boat = Boat.db().findFirst("boat_id = ?", id);
            if (boat != null) {
                boats.put(boat.id, boat);
                return boat;
            } else {
                return null;
            }
        }
    }

    public static boolean isBoatActive(String boatId) {
        Boat boat = getBoat(boatId);
        if (boat != null && boat.state == Boat.State.Active) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void awake() {
        loadBoats();
        scheduleOnce(updateInterval, "update");
        scheduleOnce(saveInterval, "save");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        if (exactlyOnce(gameMessage)) {
            if (gameMessage.boatAction != null) {
                BoatAction action = gameMessage.boatAction;

                // public
                if (gameMessage.boatAction.action == BoatAction.Action.Status) {
                    Boat boat = getBoat(gameMessage.boatAction.boat.id);
                    if (boat != null) {
                        gameMessage.boatAction.boat = boat.clone();
                        gameMessage.boatAction.boat.playerItemId = null;
                        gameMessage.boatAction.success = true;
                    } else {
                        gameMessage.boatAction.success = false;
                    }

                    setReply(gameMessage);
                    return;
                }

                // private
                if (gameMessage.boatAction.action == BoatAction.Action.Launch) {
                    if (!validate(gameMessage.boatAction.boat, false)) {
                        gameMessage.boatAction.success = false;
                        setReply(gameMessage);
                        return;
                    }

                    gameMessage.boatAction.success = launch(gameMessage.boatAction.boat);
                    setReply(gameMessage);
                    return;
                }

                // private - boat must exist
                if (!validate(gameMessage.boatAction.boat, true)) {
                    gameMessage.boatAction.success = false;
                    setReply(gameMessage);
                    return;
                }

                if (gameMessage.boatAction.action == BoatAction.Action.Dock) {
                    gameMessage.boatAction.success = setDocked(gameMessage.boatAction.boat, Boat.State.Docked);
                } else if (gameMessage.boatAction.action == BoatAction.Action.UnDock) {
                    gameMessage.boatAction.success = setDocked(gameMessage.boatAction.boat, Boat.State.Active);
                } else if (gameMessage.boatAction.action == BoatAction.Action.Recover) {
                    gameMessage.boatAction.success = recover(action.boat);
                } else if (gameMessage.boatAction.action == BoatAction.Action.SetCaptain) {
                    gameMessage.boatAction.success = setCaptain(action.boat);
                }

                setReply(gameMessage);
            }
        }
    }

    public void onTick(String message) {
        if (message.equals("update")) {
            updateBoats();
        } else if (message.equals("save")) {
            saveBoats(false);
        }
    }

    private boolean validate(Boat boat, boolean checkExists) {
        if (checkExists) {
            Boat existing = getBoat(boat.id);
            if (existing == null) {
                logger.warning("Boat does not exist: " + boat.id);
                return false;
            }
            if (!existing.ownerId.equals(characterId)) {
                logger.warning("owner id != authenticated character");
                return false;
            }
        } else {
            if (!boat.ownerId.equals(characterId)) {
                logger.warning("owner id != authenticated character");
                return false;
            }
        }


        return true;
    }

    private boolean setCaptain(Boat boat) {
        Character character = CharacterService.instance().find(boat.captainId);
        if (character == null) {
            logger.warning("Unable to find character to make captain " + boat.captainId);
            return false;
        }

        Boat current = getBoat(boat.id);
        current.captainId = boat.captainId;
        saveBoat(current);
        return true;
    }

    private boolean recover(Boat boat) {
        Boat current = getBoat(boat.id);
        if (current.state != Boat.State.Active) {
            logger.warning("Cannot recover, boat is in wrong state " + boat.state);
            return false;
        }

        Grid grid = GridService.getInstance().getGrid(current.zone, "boats");
        grid.remove(boat.id);
        boats.remove(current.id);
        Boat.db().delete(current.recordId);
        logger.warning("Boat recovered " + boat.id);
        return true;
    }

    private boolean setDocked(Boat boat, Boat.State state) {
        Boat current = getBoat(boat.id);
        BuildObject bo = BuildObjectHandler.find(boat.dockId, current.zone);
        if (bo == null) {
            logger.warning("Boat dock not found " + boat.dockId);
            return false;
        }
        current.state = state;
        saveBoat(current);
        return true;
    }

    private boolean launch(Boat boat) {
        Boat existing = getBoat(boat.id);
        if (existing != null) {
            logger.warning("Boat exists.  Recover it before relaunching");
            return false;
        }

        Character character = CharacterService.instance().find(characterId);

        PlayerItem playerItem = InventoryService.instance().find(boat.playerItemId, characterId);
        if (playerItem == null) {
            logger.warning("player item not found " + boat.playerItemId);
            return false;
        }

        boat.zone = character.zone.name;
        boat.ownerId = character.id;
        boat.captainId = character.id;
        boat.state = Boat.State.Active;
        boat.dockId = null;

        saveBoat(boat);
        return true;
    }

    private void loadBoats() {
        for (Boat boat : Boat.db().findAll()) {
            boats.put(boat.id, boat);
        }
        saveBoats(true);
    }

    private void saveBoat(Boat boat) {
        Boat.db().save(boat);
        boats.put(boat.id, boat);
    }

    private void saveBoats(boolean saveToGrid) {
        for (Boat boat : boats.values()) {
            Boat.db().save(boat);

            if (!saveToGrid) {
                continue;
            }

            if (boat.state == Boat.State.Inactive) {
                continue;
            }
            if (boat.x == 0 && boat.y == 0 && boat.z == 0) {
                continue;
            }

            Grid grid = GridService.getInstance().getGrid(boat.zone, "boats");
            TrackData trackData = grid.get(boat.id);
            if (trackData == null) {
                trackData = new TrackData();
                trackData.id = boat.id;
                trackData.entityType = TrackData.EntityType.Boat;
                trackData.characterId = boat.ownerId;
                trackData.x = boat.x;
                trackData.y = boat.y;
                trackData.z = boat.z;
                trackData.yaxis = boat.heading;
                grid.set(trackData);
                logger.warning("Set boat in grid " + boat.id);
            }
        }
        scheduleOnce(saveInterval, "save");
    }

    private void updateBoats() {
        for (Boat boat : boats.values()) {
            Grid grid = GridService.getInstance().getGrid(boat.zone, "boats");
            TrackData trackData = grid.get(boat.id);
            if (trackData == null) {
                //logger.warning("boat not found in grid "+boat.id);
                continue;
            }
            boat.x = trackData.x;
            boat.y = trackData.y;
            boat.z = trackData.z;
            boat.zone = grid.getZone();
            boat.heading = trackData.yaxis;
        }


        scheduleOnce(updateInterval, "update");
    }

}
