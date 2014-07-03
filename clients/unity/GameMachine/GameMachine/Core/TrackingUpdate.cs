using System;
using System.Collections.Generic;
using GameMachine;
using TrackExtra = GameMachine.Messages.TrackExtra;

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
		public TrackExtra trackExtra;

		public TrackingUpdate (string entityId, float x, float y, float z)
		{
			this.entityId = entityId;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}

