using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models
{
	
	public class JsonFind : JsonModel
	{
		public string test { get; set; }
		
		public static void Receive (string json)
		{
			JsonFind jsonFind = JsonConvert.DeserializeObject < JsonFind > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}