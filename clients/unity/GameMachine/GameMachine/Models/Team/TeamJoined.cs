using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class TeamJoined : JsonModel
	{
		public string name { get; set; }
		public string owner { get; set; }
		public string access { get; set; }
		public List<string> members { get; set; }
		public string invite_id { get; set; }
		
		
		
		public static void Receive (string json)
		{
			TeamJoined teamJoined = JsonConvert.DeserializeObject < TeamJoined > (json);
			TeamsManager.Instance.joinedTeam (teamJoined);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}