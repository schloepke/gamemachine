using System;
using System.Collections.Generic;
using TrackData = io.gamemachine.messages.TrackData;
using TrackDataResponse = io.gamemachine.messages.TrackDataResponse;
namespace GameMachine
{
    public interface Trackable
    {
        void TrackDataReceived (List<TrackData> trackDatas);
        TrackData UpdateTracking ();
        void HandleTrackDataResponse (TrackDataResponse response);
    }
}
