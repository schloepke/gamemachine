using System;
using System.Collections.Generic;
using GameMachine;
using TrackData = io.gamemachine.messages.TrackData;

namespace GameMachine.Core
{
	public class TrackingUpdate
	{
		public string entityId;
		public float x;
		public float y;
		public float z;
		public string entityType;
		public string neighborEntityType;
		public TrackData trackData;

		public TrackingUpdate (string entityId, float x, float y, float z)
		{
			this.entityId = entityId;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}

