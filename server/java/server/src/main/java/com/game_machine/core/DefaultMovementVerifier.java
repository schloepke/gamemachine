package com.game_machine.core;

import java.util.concurrent.ConcurrentHashMap;

import GameMachine.Messages.TrackData;

public class DefaultMovementVerifier implements MovementVerifier {

	private ConcurrentHashMap<String, TrackData> trackdatas = new ConcurrentHashMap<String, TrackData>();
	private ConcurrentHashMap<String, Long> lastUpdates = new ConcurrentHashMap<String, Long>();
	private double maxUnitsPerSecond;

	private double distance(float px, float py, float x, float y) {
		return Math.sqrt(Math.pow(x - px, 2) + Math.pow(y - py, 2));
	}

	public DefaultMovementVerifier(double maxUnitsPerSecond) {
		this.maxUnitsPerSecond = maxUnitsPerSecond;
	}

	@Override
	public boolean verify(TrackData trackData) {
		if (!trackdatas.containsKey(trackData.id)) {
			trackdatas.put(trackData.id, trackData);
			lastUpdates.put(trackData.id, System.currentTimeMillis());
			return true;
		}

		// verify once per second, every update is a waste of cpu
		Long lastUpdate = lastUpdates.get(trackData.id);
		double seconds = (System.currentTimeMillis() - lastUpdate) / 1000;
		if (seconds >= 1.0) {
			TrackData previous = trackdatas.get(trackData.id);

			// check distance traveled over time (basic speed hack prevention)
			double distance = distance(previous.x, previous.y, trackData.x, trackData.y);
			double unitsPerSecond = distance / seconds;
			if (unitsPerSecond > maxUnitsPerSecond) {
				return false;
			}

			// TODO check valid movement.
			// using pathfinding, pre calculate every possible move and save it
			// somewhere.
			// At startup load the data and use it to check for valid movement.
			// Can also modify it
			// at runtime if needed.

			// In addition, check for other objects that occupy the coordinates
			// the player is trying to move to, and
			// deny if they are occupied. Need to look at most cost effective
			// way to do this.

			trackdatas.put(trackData.id, trackData);
			lastUpdates.put(trackData.id, System.currentTimeMillis());
		}
		return true;
	}

}
