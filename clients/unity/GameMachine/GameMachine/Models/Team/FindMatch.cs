using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class FindMatch : JsonModel
	{
		public string team_name { get; set; }

		public static void Receive (string json)
		{
			FindMatch findMatch = JsonConvert.DeserializeObject < FindMatch > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}