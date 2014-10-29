using System.Collections.Generic;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;

namespace DynamicMessages
{

    [ProtoContract]
    public class PlayerMove
    {
        [ProtoMember(1)]
        public String to { get; set; }

        [ProtoMember(2)]
        public String action { get; set; }

        [ProtoMember(3)]
        public String playerId { get; set; }
    }
}