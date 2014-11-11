package io.gamemachine.core;

import GameMachine.Messages.TrackData;

public interface MovementVerifier {
	boolean verify(TrackData trackData);
}
