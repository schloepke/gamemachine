using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class CreateTeam : JsonModel
	{
		public string name { get; set; }
		public string owner { get; set; }
		public string access { get; set; }

		
		public static void Receive (string json)
		{
			CreateTeam createTeam = JsonConvert.DeserializeObject < CreateTeam > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}