using System;
using System.Collections.Generic;
using GameMachine;
using TrackExtra = GameMachine.Messages.TrackExtra;

namespace GameMachine
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

        public TrackingUpdate(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

