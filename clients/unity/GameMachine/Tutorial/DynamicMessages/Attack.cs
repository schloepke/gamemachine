using System.Collections.Generic;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;

namespace DynamicMessages
{

    [ProtoContract]
    public class Attack
    {

        [ProtoMember(1)]
        public String attacker { get; set; }

        [ProtoMember(2)]
        public String target { get; set; }

        [ProtoMember(3)]
        public String attack { get; set; }

        [ProtoMember(4)]
        public int damage { get; set; }
    }
}
