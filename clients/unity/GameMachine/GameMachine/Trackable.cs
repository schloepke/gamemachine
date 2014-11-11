using System;
using System.Collections.Generic;
using TrackData = io.gamemachine.messages.TrackData;
namespace GameMachine
{
	public interface Trackable
	{
		void TrackDataReceived (List<TrackData> trackDatas);
		TrackData UpdateTracking ();
	}
}
