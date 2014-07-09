package com.game_machine.core;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.ClientManagerEvent;
import GameMachine.Messages.Entity;
import GameMachine.Messages.Neighbor;
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
	private ArrayList<TrackData> testValues;
	
	public EntityTracking() {
		messageGateway = ActorUtil.getSelectionByName(MessageGateway.name);
		grid = Grid.find("default");
		aoeGrid = Grid.find("aoe");
		Commands.clientManagerRegister(name);
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof Entity) {
			Entity entity = (Entity)message;
			
			if (entity.hasGetNeighbors()) {
				SendNeighbors(entity);
			}
			
			if (entity.hasTrackEntity()) {
				setEntityLocation(entity);
			}
			
		} else if (message instanceof ClientManagerEvent) {
			ClientManagerEvent event = (ClientManagerEvent)message;
			if (event.event.equals("disconnected")) {
				removePlayerData(event);
			}
		} else {
			unhandled(message);
		}

	}
	
	private float randomInRange(float min, float max) {
		  return (float) (Math.random() < 0.5 ? ((1-Math.random()) * (max-min) + min) : (Math.random() * (max-min) + min));
		}
	
	
	
	
	private void removePlayerData(ClientManagerEvent event) {
		grid.remove(event.player_id);
		aoeGrid.remove(event.player_id);
	}
	
	private void SendNeighbors(Entity entity) {
		Float x = entity.getNeighbors.vector3.x;
		Float y = entity.getNeighbors.vector3.y;
		if (x == null) {
			x = 0f;
		}
		if (y == null) {
			y = 0f;
		}
		
		ArrayList<TrackData> trackDatas = grid.neighbors(x, y, entity.getNeighbors.neighborType);
		
		if (trackDatas.size() >= 1) {
			toNeighbors(entity.player,trackDatas);
		}
	}
		
	private void SendToGateway(Player player,Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		playerMessage.setPlayer(player);
		playerMessage.setId(player.id);
		messageGateway.tell(playerMessage, getSelf());
	}
	
	private void toNeighbors(Player player,ArrayList<TrackData> trackDatas) {
		int count = 0;
		Neighbors neighbors = new Neighbors();
		
		for (TrackData trackData : trackDatas) {
			neighbors.addTrackData(trackData);
			
			count++;
			if (count >= 30) {
				SendToGateway(player,neighbors);
				count = 0;
				neighbors = new Neighbors();
			}
		}
		SendToGateway(player,neighbors);
	}
	
	private void setEntityLocation(Entity entity) {
		
		GameMachine.Messages.Vector3 vector = entity.vector3;
		
		// So either protostuff or protobuf-net has a bug where 0 floats come through as null
		// This is *really* annoying must track down
		if (vector.x == null) {
			vector.x = 0f;
		}
		if (vector.y == null) {
			vector.y = 0f;
		}
		if (vector.z == null) {
			vector.z = 0f;
		}
		
		grid.set(entity.id, vector.x, vector.y, vector.z, entity.entityType);
		aoeGrid.set(entity.id, vector.x, vector.y, vector.z, entity.entityType);
	}

}
