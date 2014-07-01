using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class Teams : JsonModel
	{
		public List<Team> teams { get; set; }

		
		
		public static void Receive (string json)
		{
			Teams teams = JsonConvert.DeserializeObject < Teams > (json);
			TeamsManager.Instance.teamsReceived (teams);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}