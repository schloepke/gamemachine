package com.game_machine.core;

import java.util.ArrayList;

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

	private Grid aoeGrid;
	private Grid grid;
	private ActorSelection messageGateway;

	public EntityTracking() {
		messageGateway = ActorUtil.getSelectionByName(MessageGateway.name);
		grid = Grid.find("default");
		aoeGrid = Grid.find("aoe");
		Commands.clientManagerRegister(name);
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Entity) {
			Entity entity = (Entity) message;

			SendNeighbors(entity);
			setEntityLocation(entity.trackData);

		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent) message;
			if (event.event.equals("disconnected")) {
				removePlayerData(event);
			}
		} else {
			unhandled(message);
		}

	}

	private void removePlayerData(ClientManagerEvent event) {
		grid.remove(event.player_id);
		aoeGrid.remove(event.player_id);
	}

	private void SendNeighbors(Entity entity) {
		Float x = entity.trackData.x;
		Float y = entity.trackData.y;
		if (x == null) {
			x = 0f;
		}
		if (y == null) {
			y = 0f;
		}

		ArrayList<TrackData> trackDatas = grid.neighbors(x, y,
				entity.trackData.neighborEntityType);

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

	private void setEntityLocation(TrackData trackData) {
		
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

		grid.set(trackData);
		aoeGrid.set(trackData);
	}

}
