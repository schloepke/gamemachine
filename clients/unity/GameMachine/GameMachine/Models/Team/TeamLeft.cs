using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class TeamLeft : JsonModel
	{
		public string name { get; set; }

		public static void Receive (string json)
		{
			TeamLeft teamLeft = JsonConvert.DeserializeObject < TeamLeft > (json);
			TeamsManager.Instance.leftTeam (teamLeft);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}