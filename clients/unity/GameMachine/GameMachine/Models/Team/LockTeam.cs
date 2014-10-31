using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
    
    public class LockTeam : JsonModel
    {
        public string name { get; set; }
        
        public static void Receive (string json)
        {
            LockTeam lockTeam = JsonConvert.DeserializeObject < LockTeam > (json);
        }
        
        public override string ToJson ()
        {
            return JsonConvert.SerializeObject (this, Formatting.Indented);
        }
    }
}