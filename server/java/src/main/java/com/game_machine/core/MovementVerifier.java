package com.game_machine.core;

import GameMachine.Messages.TrackData;

public interface MovementVerifier {
	boolean verify(TrackData trackData);
}
