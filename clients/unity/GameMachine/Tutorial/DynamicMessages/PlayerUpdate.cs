using System.Collections.Generic;
using System.Collections;
using System;
using System.IO;
using ProtoBuf;

namespace DynamicMessages
{
    [ProtoContract]
    public class PlayerUpdate
    {
        [ProtoMember(1)]
        public List<Vitals>
            vitals = new List<Vitals> ();
    }
}
