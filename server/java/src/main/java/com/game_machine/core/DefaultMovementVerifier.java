package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.TrackData;

public class DefaultMovementVerifier implements MovementVerifier {

	private ConcurrentHashMap<String, TrackData> trackdatas = new ConcurrentHashMap<String, TrackData>();
	
	
	@Override
	public boolean verify(TrackData trackData) {
		if (!trackdatas.containsKey(trackData.id)) {
			trackdatas.put(trackData.id, trackData);
			return true;
		}
		
		TrackData previous = trackdatas.get(trackData.id);
		
		// TODO check distance traveled over time (speed hack prevention)
		
		// TODO check position to see if it's in a walkable area of the map
		
		return true;
	}

}
