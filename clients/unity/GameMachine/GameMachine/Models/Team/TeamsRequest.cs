using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
    public class TeamsRequest : JsonModel
    {
        public string filter { get; set; }
        public Dictionary<string,int> skills = new Dictionary<string,int> ();


        public static void Receive (string json)
        {
            TeamsRequest teamsRequest = JsonConvert.DeserializeObject < TeamsRequest > (json);
        }
		
        public override string ToJson ()
        {
            return JsonConvert.SerializeObject (this, Formatting.Indented);
        }
    }
}