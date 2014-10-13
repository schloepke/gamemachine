package com.game_machine.core;

import java.util.ArrayList;
import java.util.HashMap;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Neighbors;
import GameMachine.Messages.Player;
import GameMachine.Messages.TrackData;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class EntityTracking extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	public static String name = "fastpath_entity_tracking";

	private Grid defaultGrid;
	private ActorSelection messageGateway;

	public EntityTracking() {
		messageGateway = ActorUtil.getSelectionByName(MessageGateway.name);
		defaultGrid = Grid.find("default");
		Commands.clientManagerRegister(name);
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Entity) {
			Entity entity = (Entity) message;

			Grid grid = gameGrid(entity.player.id);
			if (grid == null) {
				log.warning("No grid found for " + entity.player.id);
				return;
			}

			SendNeighbors(grid, entity);
			setEntityLocation(grid, entity.trackData);

		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent) message;
			if (event.event.equals("disconnected")) {
				removePlayerData(event);
			}
		} else {
			unhandled(message);
		}

	}

	private Grid gameGrid(String playerId) {
		String gameId = PlayerService.getInstance().getGameId(playerId);
		if (gameId == null) {
			return null;
		} else {
			Grid gameGrid = Grid.find(gameId);
			if (gameGrid == null) {
				gameGrid = Grid.findOrCreate(gameId, defaultGrid.getMax(), defaultGrid.getCellSize());
			}

			return gameGrid;
		}
	}

	private void removePlayerData(ClientManagerEvent event) {
		Grid grid = gameGrid(event.player_id);
		if (grid != null) {
			gameGrid(event.player_id).remove(event.player_id);
		}
	}

	private void SendNeighbors(Grid grid, Entity entity) {
		Float x = entity.trackData.x;
		Float y = entity.trackData.y;
		if (x == null) {
			x = 0f;
		}
		if (y == null) {
			y = 0f;
		}

		ArrayList<TrackData> trackDatas = grid.neighbors(x, y, entity.trackData.neighborEntityType);

		if (trackDatas.size() >= 1) {
			toNeighbors(entity.player, trackDatas);
		}
	}

	private void SendToGateway(Player player, Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		playerMessage.setPlayer(player);
		playerMessage.setId(player.id);
		messageGateway.tell(playerMessage, getSelf());
	}

	private void toNeighbors(Player player, ArrayList<TrackData> trackDatas) {
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

		// TODO. If set returns false send message to the client letting them
		// know their movement was not allowed
		grid.set(trackData);
	}

}
