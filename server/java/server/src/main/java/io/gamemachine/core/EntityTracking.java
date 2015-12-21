package io.gamemachine.core;

import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.AgentTrackData;
import io.gamemachine.messages.ClientManagerEvent;
import io.gamemachine.messages.DynamicMessage;
import io.gamemachine.messages.Entity;
import io.gamemachine.messages.Neighbors;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.TrackDataResponse;
import io.gamemachine.messages.TrackDataUpdate;
import io.gamemachine.messages.Zone;
import io.gamemachine.messages.TrackData.EntityType;
import io.gamemachine.objectdb.Cache;
import io.gamemachine.regions.ZoneService;

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

	public static String name = "fastpath_entity_tracking";

	private PlayerService playerService;
	
	public EntityTracking() {
		Commands.clientManagerRegister(name);
		playerService = PlayerService.getInstance();
	}

	
	private void updateTrackData(TrackDataUpdate update, Player player) {
		if (player.role == Player.Role.Player) {
			return;
		}
		
	}

	private boolean isValid(TrackData trackData,Player player) {
		if (!player.id.equals(trackData.id)) {
			return false;
		}
		
		if (playerService.isAuthenticated(player.id)) {
			return true;
		} else if (player.role == Player.Role.AgentController) {
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
			agentGrid = GridService.getInstance().getPlayerGrid(trackData.gridName, player.id);
			setEntityLocation(player.id, agentGrid, trackData);
		}
	}

	private void handleTrackData(TrackData trackData, Player player) {
		Grid grid = GridService.getInstance().getPlayerGrid(trackData.gridName, player.id);

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
					PlayerMessage.tell(playerMessage, tdata.id);
					// logger.warn("Broadcast sent to " + tdata.id);
				}
			}
			return;
		}

		if (player.role == Player.Role.Player) {
			setEntityLocation(player.id, grid, trackData);
		}
		

		if (trackData.getNeighbors >= 1) {
			SendNeighbors(grid, trackData.x, trackData.z, player, trackData.neighborEntityType, trackData.getNeighbors);
		}
	}

	private void removePlayerData(ClientManagerEvent event) {
		GridService.getInstance().removeEntityFromGrids(event.player_id);
	}

	private void SendNeighbors(Grid grid, int x, int z, Player player, EntityType neighborType, int neighborsFlag) {
		List<TrackData> trackDatas;
//		boolean isAgentController = (player.role == Player.Role.AgentController);
//
//		if (neighborType != null && neighborType == EntityType.All) {
//			// Only agents have access to entire grid
//			if (!isAgentController) {
//				logger.warn("Unauthorized attempt to get entire grid");
//				return;
//			}
//			trackDatas = grid.getAll();
//		} else {
//			trackDatas = grid.neighbors(player.id, x, z, neighborType, neighborsFlag);
//		}

		trackDatas = grid.neighbors(player.id, x, z, neighborType, neighborsFlag);
		
		if (trackDatas.size() >= 1) {
			toNeighbors(player, trackDatas, false);
		}
	}

	private void SendToGateway(Player player, Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		//playerMessage.setPlayer(player);
		//playerMessage.setId(player.id);
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

	private void sendTrackDataResponse(String playerId, String id, TrackDataResponse.REASON reason) {
		Entity entity = new Entity();
		entity.id = "0";
		TrackDataResponse response = new TrackDataResponse();
		response.id = id;
		response.reason = reason;
		entity.setTrackDataResponse(response);
		PlayerMessage.tell(entity, playerId);
	}
	
	private void setEntityLocation(String playerId, Grid grid, TrackData trackData) {


		if (trackData.entityType == EntityType.Player) {
			if (!trackData.id.equals(playerId)) {
				return;
			}
			if (movementVerifier != null) {
				if (!movementVerifier.verify(trackData)) {
					sendTrackDataResponse(playerId, trackData.id, TrackDataResponse.REASON.VALIDATION_FAILED);
					return;
				}
			}
		}
		
		if (!grid.set(trackData)) {

			// Resend is most likely from a TrackData that contains a delta, but
			// where we never received an initial
			// TrackData with the full coordinates.
			sendTrackDataResponse(playerId, trackData.id, TrackDataResponse.REASON.RESEND);
		}
	}

}
