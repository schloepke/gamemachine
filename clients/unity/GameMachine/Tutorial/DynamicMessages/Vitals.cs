using System.Collections.Generic;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;

namespace DynamicMessages
{
    [ProtoContract]
    public class Vitals
    {

        [ProtoMember(1)]
        public String id { get; set; }
        [ProtoMember(2)]
        public int health { get; set; }
        [ProtoMember(3)]
        public String entityType { get; set; }
        [ProtoMember(4)]
        public float x { get; set; }
        [ProtoMember(5)]
        public float y { get; set; }
        [ProtoMember(6)]
        public float z { get; set; }
    }
}
