package com.game_machine.core;

import java.util.List;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Neighbors;
import GameMachine.Messages.Player;
import GameMachine.Messages.TrackData;
import GameMachine.Messages.TrackData.EntityType;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class EntityTracking extends UntypedActor {

	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	public static String name = "fastpath_entity_tracking";

	private ActorSelection messageGateway;

	public EntityTracking() {
		messageGateway = ActorUtil.getSelectionByName(MessageGateway.name);
		Commands.clientManagerRegister(name);
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Entity) {
			Entity entity = (Entity) message;

			if (entity.hasAgentTrackData() && entity.getAgentTrackData().getTrackDataCount() >= 1) {
				Grid agentGrid;
				for (TrackData trackData : entity.getAgentTrackData().getTrackDataList()) {
					agentGrid = gameGrid(entity.player.id, trackData.getGridName());
					setEntityLocation(agentGrid, trackData);
				}
				return;
			}

			Grid grid = gameGrid(entity.player.id, entity.trackData.getGridName());

			if (grid == null) {
				logger.warning("No grid found for " + entity.player.id);
				return;
			}

			Player player = PlayerService.getInstance().find(entity.player.getId());
			if (player == null) {
				logger.warning("Player for " + entity.player.getId() + " is null");
				return;
			}

			setEntityLocation(grid, entity.trackData);

			if (entity.trackData.hasGetNeighbors() && entity.trackData.getNeighbors == 1) {
				SendNeighbors(grid, entity.trackData.x, entity.trackData.y, player, entity.trackData.getNeighborEntityType());
			}

		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent) message;
			if (event.event.equals("disconnected")) {
				removePlayerData(event);
			}
		} else {
			unhandled(message);
		}
	}

	public static Grid gameGrid(String playerId, String name) {
		if (name == null) {
			name = "default";
		}
		String gameId = PlayerService.getInstance().getGameId(playerId);
		if (gameId == null) {
			return null;
		} else {
			return Grid.getGameGrid(gameId, name);
		}
	}

	private void removePlayerData(ClientManagerEvent event) {
		String gameId = PlayerService.getInstance().getGameId(event.player_id);
		if (gameId == null) {
			return;
		}
		if (Grid.gameGrids.containsKey(gameId)) {
			for (String name : Grid.gameGrids.get(gameId).keySet()) {
				Grid grid = gameGrid(event.player_id, name);
				if (grid != null) {
					logger.debug("Removing " + event.player_id + " from grid " + name);
					grid.remove(event.player_id);
				}
			}
		}

	}

	private void SendNeighbors(Grid grid, float x, float y, Player player, EntityType neighborType) {
		List<TrackData> trackDatas;
		if (neighborType != null && neighborType == EntityType.ALL) {
			// Only agents have access to entire grid
			if (!player.getRole().equals("agent_controller")) {
				return;
			}
			trackDatas = grid.getAll();
		} else {
			trackDatas = grid.neighbors(x, y, neighborType);
		}

		if (trackDatas.size() >= 1) {
			toNeighbors(player, trackDatas);
		}
	}

	private void SendToGateway(Player player, Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		playerMessage.setPlayer(player);
		playerMessage.setId(player.id);
		messageGateway.tell(playerMessage, getSelf());
	}

	private void toNeighbors(Player player, List<TrackData> trackDatas) {
		int count = 0;
		Neighbors neighbors = new Neighbors();

		for (TrackData trackData : trackDatas) {
			neighbors.addTrackData(trackData);

			count++;
			if (count >= 30) {
				SendToGateway(player, neighbors);
				count = 0;
				neighbors = new Neighbors();
			}
		}
		SendToGateway(player, neighbors);
	}

	private void setEntityLocation(Grid grid, TrackData trackData) {

		// So either protostuff or protobuf-net has a bug where 0 floats come
		// through as null
		// This is *really* annoying must track down
		if (trackData.x == null) {
			trackData.x = 0f;
		}
		if (trackData.y == null) {
			trackData.y = 0f;
		}
		if (trackData.z == null) {
			trackData.z = 0f;
		}
		
		// Allows for getting neighbors without being added to the grid (ie agent controllers)
		if (trackData.x == -1f && trackData.y == -1f) {
			return;
		}
		
		// TODO. If set returns false send message to the client letting them
		// know their movement was not allowed
		grid.set(trackData);
	}

}
