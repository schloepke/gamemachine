using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class DestroyTeam : JsonModel
	{
		public string name { get; set; }
		public string owner { get; set; }

		
		public static void Receive (string json)
		{
			DestroyTeam destroyTeam = JsonConvert.DeserializeObject < DestroyTeam > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}