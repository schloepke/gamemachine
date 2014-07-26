using System;
using System.Collections.Generic;
using TrackData = GameMachine.Messages.TrackData;
namespace GameMachine
{
	public interface Trackable
	{
		void TrackDataReceived (List<TrackData> trackDatas);
		TrackData UpdateTracking ();
	}
}
