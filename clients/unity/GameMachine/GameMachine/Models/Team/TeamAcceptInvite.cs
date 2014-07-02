using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class TeamAcceptInvite : JsonModel
	{
		public string name { get; set; }
		public string invite_id { get; set; }
		
		public static void Receive (string json)
		{
			TeamAcceptInvite teamAcceptInvite = JsonConvert.DeserializeObject < TeamAcceptInvite > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}