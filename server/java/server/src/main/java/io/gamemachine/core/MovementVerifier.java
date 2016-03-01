package io.gamemachine.core;

import io.gamemachine.messages.TrackData;

public interface MovementVerifier {
    boolean verify(TrackData trackData);
}
