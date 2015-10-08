package io.gamemachine.core;

import io.gamemachine.messages.AgentTrackData;
import io.gamemachine.messages.ClientManagerEvent;
import io.gamemachine.messages.DynamicMessage;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.Neighbors;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.TrackDataResponse;
import io.gamemachine.messages.TrackDataUpdate;
import io.gamemachine.messages.TrackData.EntityType;
import io.gamemachine.objectdb.Cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public class EntityTracking extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(EntityTracking.class);
	private static MovementVerifier movementVerifier = null;
	private static Map<String, Cache<String, DynamicMessage>> dynamicMessageCaches = new ConcurrentHashMap<String, Cache<String, DynamicMessage>>();

	public static String name = "fastpath_entity_tracking";

	private PlayerService playerService;
	private String role;
	
	public EntityTracking() {
		Commands.clientManagerRegister(name);
		playerService = PlayerService.getInstance();
	}

	private static Cache<String, DynamicMessage> getDynamicMessageCache(String gameId) {
		Cache<String, DynamicMessage> cache = dynamicMessageCaches.get(gameId);
		if (cache == null) {
			cache = new Cache<String, DynamicMessage>(120, 10000);
			dynamicMessageCaches.put(gameId, cache);
		}
		return cache;
	}

	private void updateTrackData(TrackDataUpdate update, Player player) {
		String gameId = playerService.getGameId(player.id);
		// No access for clients
		if (player.role.equals("player")) {
			return;
		}
		Cache<String, DynamicMessage> cache = getDynamicMessageCache(gameId);
		cache.set(update.id, update.dynamicMessage);
	}

	private boolean isValid(TrackData trackData,Player player) {
		if (!player.id.equals(trackData.id)) {
			return false;
		}
		
		if (playerService.isAuthenticated(player.id)) {
			return true;
		} else if (player.role.equals("agent_controller")) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		Player player;

		if (message instanceof TrackData) {
			TrackData trackData = (TrackData) message;
			player = playerService.find(trackData.id);
			if (player == null) {
				logger.warn("Player for " + trackData.id + " is null");
				return;
			} else if (!isValid(trackData,player)) {
				logger.warn("Invalid request trackdata.id=" + trackData.id + " playerId="+player.id);
				return;
			}
			role = player.role;
			handleTrackData(trackData, player);
			return;
		}

		if (message instanceof Entity) {
			Entity entity = (Entity) message;

			player = playerService.find(entity.player.id);
			if (player == null) {
				logger.warn("Player for " + entity.player.id + " is null");
				return;
			}
			role = player.role;
			
			if (entity.agentTrackData != null) {
				handleAgentTrackData(entity.agentTrackData, player);
				return;
			}

			if (entity.trackDataUpdate != null) {
				updateTrackData(entity.getTrackDataUpdate(), player);
				return;
			}

			if (!isValid(entity.trackData,player)) {
				logger.warn("Invalid request trackdata.id=" + entity.trackData.id + " playerId="+player.id);
				return;
			}
			
			handleTrackData(entity.trackData, player);

		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent) message;
			if (event.event.equals("disconnected")) {
				removePlayerData(event);
			}
		} else {
			unhandled(message);
		}
	}

	private void handleAgentTrackData(AgentTrackData agentTrackData, Player player) {
		Grid agentGrid;
		for (TrackData trackData : agentTrackData.getTrackDataList()) {
			agentGrid = gameGrid(player.id, trackData.gridName);
			setEntityLocation(player.id, agentGrid, trackData);
		}
	}

	private void handleTrackData(TrackData trackData, Player player) {
		Grid grid = gameGrid(player.id, trackData.gridName);

		if (grid == null) {
			logger.warn("No grid found for " + player.id);
			return;
		}

		if (trackData.broadcast == 1) {
			List<TrackData> trackDatas = grid.neighbors(player.id, trackData.x, trackData.y,
					trackData.neighborEntityType, 2);

			if (trackDatas == null) {
				return;
			}

			TrackData broadcastTrackData = new TrackData();
			broadcastTrackData.x = trackData.x;
			broadcastTrackData.y = trackData.y;
			broadcastTrackData.z = trackData.z;
			broadcastTrackData.userDefinedData = trackData.userDefinedData;
			broadcastTrackData.broadcast = 1;
			broadcastTrackData.id = player.id;
			broadcastTrackData.entityType = TrackData.EntityType.Player;

			for (TrackData tdata : trackDatas) {
				if (tdata.entityType == TrackData.EntityType.Player) {
					Entity playerMessage = new Entity();
					playerMessage.id = "b";
					Neighbors neighbors = new Neighbors();
					neighbors.addTrackData(broadcastTrackData);
					playerMessage.setNeighbors(neighbors);
					PlayerCommands.sendToPlayer(playerMessage, tdata.id);
					// logger.warn("Broadcast sent to " + tdata.id);
				}
			}
			return;
		}

		setEntityLocation(player.id, grid, trackData);

		if (trackData.getNeighbors >= 1) {
			SendNeighbors(grid, trackData.x, trackData.y, player, trackData.neighborEntityType, trackData.getNeighbors);
		}
	}

	private Grid gameGrid(String playerId, String name) {
		if (name == null) {
			name = "default";
		}
		String gameId = playerService.getGameId(playerId);
		if (gameId == null) {
			return null;
		} else {
			return GameGrid.getGameGrid(gameId, name, playerId);
		}
	}

	private void removePlayerData(ClientManagerEvent event) {
		String gameId = playerService.getGameId(event.player_id);
		if (gameId == null) {
			return;
		}
		for (Grid grid : GameGrid.getGridList()) {
			logger.debug("Removing " + event.player_id + " from grid " + name);
			grid.remove(event.player_id);
		}

	}

	private void SendNeighbors(Grid grid, int x, int y, Player player, EntityType neighborType, int neighborsFlag) {
		List<TrackData> trackDatas;
		boolean isAgentController = (player.getRole().equals("agent_controller"));

		if (neighborType != null && neighborType == EntityType.All) {
			// Only agents have access to entire grid
			if (!isAgentController) {
				logger.warn("Unauthorized attempt to get entire grid");
				return;
			}
			trackDatas = grid.getAll();
		} else {
			trackDatas = grid.neighbors(player.id, x, y, neighborType, neighborsFlag);
		}

		if (trackDatas.size() >= 1) {
			toNeighbors(player, trackDatas, isAgentController);
		} else {
			// logger.warn("No Neighbors");
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
		int count = 0;

		for (TrackData trackData : trackDatas) {
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


		if (trackData.entityType == EntityType.Player) {
			if (!trackData.id.equals(playerId)) {
				return;
			}
			if (movementVerifier != null) {
				if (!movementVerifier.verify(trackData)) {
					PlayerCommands.sendTrackDataResponse(playerId, trackData.id,
							TrackDataResponse.REASON.VALIDATION_FAILED);
					return;
				}
			}
		}

		String gameId = playerService.getGameId(playerId);
		Cache<String, DynamicMessage> cache = getDynamicMessageCache(gameId);
		DynamicMessage dynamicMessage = cache.get(trackData.getId());
		if (dynamicMessage != null) {
			trackData.setDynamicMessage(dynamicMessage);
		}

		if (trackData.zone > 0) {
			GameGrid.setEntityZone(trackData.id, trackData.zone);
		}

		if (!grid.set(trackData)) {

			// Resend is most likely from a TrackData that contains a delta, but
			// where we never received an initial
			// TrackData with the full coordinates.
			PlayerCommands.sendTrackDataResponse(playerId, trackData.id, TrackDataResponse.REASON.RESEND);
		}
	}

}
