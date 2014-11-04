using System.Collections.Generic;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;

namespace DynamicMessages
{
    [ProtoContract]
    public class GameClientEntity
    {

        [ProtoMember(1)]
        public int health { get; set; }

    }
}
