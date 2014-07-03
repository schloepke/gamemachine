using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class Team : JsonModel
	{
		public string name { get; set; }
		public string team_id { get; set; }
		public string owner { get; set; }
		public string access { get; set; }
		public List<string> members { get; set; }
		public string invite_id { get; set; }

		

		public static void Receive (string json)
		{
			Team team = JsonConvert.DeserializeObject < Team > (json);
			TeamsManager.Instance.teamReceived (team);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}