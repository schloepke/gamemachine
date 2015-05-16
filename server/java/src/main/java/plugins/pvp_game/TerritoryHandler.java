package plugins.pvp_game;

import io.gamemachine.core.GameGrid;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Grid;
import io.gamemachine.core.PlayerCommands;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Territories;
import io.gamemachine.messages.Territory;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.WorldObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class TerritoryHandler extends GameMessageActor {

	public static String name = TerritoryHandler.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

	public static ConcurrentHashMap<String, Territory> territories = new ConcurrentHashMap<String, Territory>();
	
	private void createTerritories() {
		addTerritory("Highlands", GuildHandler.npcGuild, "k1");
		addTerritory("Central Garrison", GuildHandler.npcGuild, "k3");
		addTerritory("Wildlands", GuildHandler.npcGuild, "k2");
		addTerritory("Jungle Region", GuildHandler.npcGuild, "k4");
	}

	private void addTerritory(String id, String owner, String keep) {
		Territory territory = find(id, keep);
		if (territory == null) {
			territory = new Territory();
			territory.id = id;
			territory.owner = owner;
			territory.keep = keep;
			save(territory);
		}
		territories.put(territory.id, territory);
	}

	private Territory find(String id, String keep) {
		if (!territories.containsKey(id)) {
			Territory territory = Territory.db().findFirst("territory_id = ? and territory_keep = ?", id, keep);
			if (territory == null) {
				return null;
			} else {
				territories.put(id, territory);
			}
		}
		return territories.get(id);
	}

	private void save(Territory territory) {
		Territory.db().save(territory);
		territories.put(territory.id, territory);
	}

	private void sendTerritories() {
		GameMessage gameMessage = new GameMessage();
		gameMessage.territories = new Territories();
		gameMessage.territories.territories = new ArrayList<Territory>(territories.values());

		for (Grid grid : GameGrid.gridsStartingWith("default")) {
			for (TrackData trackData : grid.getAll()) {
				PlayerCommands.sendGameMessage(gameMessage, trackData.id);
			}
		}
	}

	public static void removeOwner(String owner) {
		for (WorldObject worldObject : WorldObject.db().where("world_object_owner_id = ?", owner)) {
			worldObject.ownerId = GuildHandler.npcGuild;
			WorldObject.db().save(worldObject);
			ConsumableItemHandler.wobjects.put(worldObject.id, worldObject);
		}
		for (Territory territory : Territory.db().where("territory_owner = ?", owner)) {
			territory.owner = GuildHandler.npcGuild;
			Territory.db().save(territory);
			territories.put(territory.id, territory);
		}
	}
	
	@Override
	public void awake() {
		createTerritories();
		scheduleOnce(10000, "tick");
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		if (exactlyOnce(gameMessage)) {
			if (gameMessage.hasTerritory()) {
				Territory territory = find(gameMessage.territory.id, gameMessage.territory.keep);
				if (territory != null) {
					territory.owner = gameMessage.territory.owner;
					territory.keep = gameMessage.territory.keep;
					logger.warning("territory " + territory.id + " changing ownership to " + territory.owner);
					save(territory);
					for (WorldObject worldObject : WorldObject.db().where("world_object_parent_id = ?", territory.keep)) {
						worldObject.ownerId = territory.owner;
						worldObject.health = worldObject.maxHealth;
						WorldObject.db().save(worldObject);
						ConsumableItemHandler.wobjects.put(worldObject.id, worldObject);
					}
					
					sendTerritories();
				}
			}
			setReply(gameMessage);
		}

	}

	@Override
	public void onTick(String message) {
		sendTerritories();
		scheduleOnce(10000, "tick");

	}

}
