package com.game_machine.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game_machine.objectdb.Cache;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.Entity;
import GameMachine.Messages.DynamicMessage;
import GameMachine.Messages.Neighbors;
import GameMachine.Messages.Player;
import GameMachine.Messages.TrackData;
import GameMachine.Messages.TrackData.EntityType;
import GameMachine.Messages.TrackDataUpdate;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public class EntityTracking extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(EntityTracking.class);
	private static MovementVerifier movementVerifier = null;
	private static Cache<String,DynamicMessage> dynamicMessageCache = new Cache<String,DynamicMessage>(120, 10000);
	
	public static String name = "fastpath_entity_tracking";

	public EntityTracking() {
		Commands.clientManagerRegister(name);
	}

	public static void updateTrackData(TrackDataUpdate update) {
		dynamicMessageCache.set(update.getId(), update.getDynamicMessage());
	}
	
	private void updateTrackData(TrackDataUpdate update, Player player) {
		
		// No access for clients
		if (player.getRole().equals("player")) {
			return;
		}
		updateTrackData(update);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TrackData) {
			handleTrackData((TrackData)message);
			return;
		}
		
		if (message instanceof Entity) {
			Entity entity = (Entity) message;
			
			if (entity.hasAgentTrackData()) {
				Grid agentGrid;
				for (TrackData trackData : entity.getAgentTrackData().getTrackDataList()) {
					agentGrid = gameGrid(entity.player.id, trackData.getGridName());
					setEntityLocation(entity.player.id,agentGrid, trackData);
				}
				return;
			}

			Player player = PlayerService.getInstance().find(entity.player.getId());
			if (player == null) {
				logger.warn("Player for " + entity.player.getId() + " is null");
				return;
			}
			
			if (entity.hasTrackDataUpdate()) {
				updateTrackData(entity.getTrackDataUpdate(),player);
				return;
			}
			
			Grid grid = gameGrid(entity.player.id, entity.trackData.getGridName());

			if (grid == null) {
				logger.warn("No grid found for " + entity.player.id);
				return;
			}
			
			
			setEntityLocation(entity.player.id,grid, entity.trackData);

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

	public void handleTrackData(TrackData trackData) {
		Player player = PlayerService.getInstance().find(trackData.getId());
		if (player == null) {
			logger.warn("Player for " + trackData.getId() + " is null");
			return;
		}
		
		Grid grid = gameGrid(player.id, trackData.getGridName());

		if (grid == null) {
			logger.warn("No grid found for " + player.id);
			return;
		}
		
		setEntityLocation(player.id,grid, trackData);

		if (trackData.hasGetNeighbors() && trackData.getNeighbors == 1) {
			SendNeighbors(grid, trackData.x, trackData.y, player, trackData.getNeighborEntityType());
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
			return GameGrid.getGameGrid(gameId, name);
		}
	}

	private void removePlayerData(ClientManagerEvent event) {
		String gameId = PlayerService.getInstance().getGameId(event.player_id);
		if (gameId == null) {
			return;
		}
		
		Map<String, ConcurrentHashMap<String, Grid>> gameGrids = GameGrid.getGameGrids();
		if (gameGrids.containsKey(gameId)) {
			for (String name : gameGrids.get(gameId).keySet()) {
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
		boolean isAgentController = (player.getRole().equals("agent_controller"));
		
		if (neighborType != null && neighborType == EntityType.ALL) {
			// Only agents have access to entire grid
			if (!isAgentController) {
				return;
			}
			trackDatas = grid.getAll();
		} else {
			trackDatas = grid.neighbors(x, y, neighborType);
		}

		if (trackDatas.size() >= 1) {
			toNeighbors(player, trackDatas, isAgentController);
		}
	}

	private void SendToGateway(Player player, Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		playerMessage.setPlayer(player);
		playerMessage.setId(player.id);
		ActorSelection sel = ActorUtil.getSelectionByName(player.id);
		sel.tell(playerMessage, getSelf());
	}

	private void toNeighbors(Player player, List<TrackData> trackDatas, boolean isAgentController) {
		Neighbors neighbors = new Neighbors();
		int size = 30;
		if (isAgentController) {
			size = 100;
		}
		
		int count = 0;

		DynamicMessage dynamicMessage;
		for (TrackData trackData : trackDatas) {
			dynamicMessage = dynamicMessageCache.get(trackData.getId());
			if (dynamicMessage != null) {
				trackData.setDynamicMessage(dynamicMessage);
			}
			
			neighbors.addTrackData(trackData);

			count++;
			if (count >= size) {
				SendToGateway(player, neighbors);
				count = 0;
				neighbors = new Neighbors();
			}
		}
		SendToGateway(player, neighbors);
	}

	private void setEntityLocation(String playerId, Grid grid, TrackData trackData) {

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
		
		if (trackData.x == -1f && trackData.y == -1f) {
			grid.remove(trackData.id);
			return;
		}
		
		if (trackData.entityType == EntityType.PLAYER) {
			if (!trackData.id.equals(playerId)) {
				return;
			}
			if (movementVerifier != null) {
				if (!movementVerifier.verify(trackData)) {
					return;
				}
			}
		}
		
		grid.set(trackData);
	}

}
