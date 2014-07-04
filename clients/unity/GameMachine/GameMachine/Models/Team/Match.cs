using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class Match : JsonModel
	{
		public string server { get; set; }
		public string game_handler { get; set; }
		
		public static void Receive (string json)
		{
			Match match = JsonConvert.DeserializeObject < Match > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}