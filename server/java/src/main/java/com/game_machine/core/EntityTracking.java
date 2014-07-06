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
	public static ConcurrentHashMap<String, TrackData> trackdatas = new ConcurrentHashMap<String, TrackData>();
	public static String name = "fastpath_entity_tracking";
	
	private Grid aoeGrid;
	private Grid grid;
	private ActorSelection messageGateway;
	private ArrayList<GridValue> testValues;
	
	public EntityTracking() {
		messageGateway = ActorUtil.getSelectionByName(MessageGateway.name);
		grid = Grid.find("default");
		aoeGrid = Grid.find("aoe");
		Commands.clientManagerRegister(name);
		testValues = getTestValues();
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
	
	
	private ArrayList<GridValue> getTestValues() {
		
		ArrayList<GridValue> values = new ArrayList<GridValue>();
		for(Integer i=1; i<100; i++){
			Float id = randomInRange(1f,5000f);
			GridValue v = new GridValue(Float.toString(id), 1,randomInRange(1f,2000f), randomInRange(1f,2000f), randomInRange(1f,2000f), "npc");
			values.add(v);
		}
		return values;
	}
	
	private void removePlayerData(ClientManagerEvent event) {
		grid.remove(event.player_id);
		aoeGrid.remove(event.player_id);
		trackdatas.remove(event.player_id);
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
		
		
		ArrayList<GridValue> searchResults = grid.neighbors(x, y, entity.getNeighbors.neighborType);
		
		if (searchResults.size() >= 1) {
			gridValuesToNeighbors(entity.player,searchResults);
		}
	}
		
	private void SendToGateway(Player player,Neighbors neighbors) {
		Entity playerMessage = new Entity();
		playerMessage.setNeighbors(neighbors);
		playerMessage.setPlayer(player);
		playerMessage.setId(player.id);
		messageGateway.tell(playerMessage, getSelf());
	}
	
	private void gridValuesToNeighbors(Player player,ArrayList<GridValue> searchResults) {
		int count = 0;
		Neighbors neighbors = new Neighbors();
		
		for (GridValue gridvalue : searchResults) {
			Neighbor neighbor = new Neighbor();
			if (trackdatas.containsKey(gridvalue.id)) {
				neighbor.trackData = trackdatas.get(gridvalue.id);
			}
			neighbor.id = gridvalue.id;
			neighbor.location = new GameMachine.Messages.Vector3();
			neighbor.location.x = gridvalue.x;
			neighbor.location.y = gridvalue.y;
			neighbor.location.z = gridvalue.z;
			neighbors.addNeighbor(neighbor);
			
			count++;
			if (count >= 20) {
				SendToGateway(player,neighbors);
				count = 0;
				neighbors = new Neighbors();
			}
		}
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
		trackdatas.put(entity.id, entity.trackEntity.trackData);
	}

}
