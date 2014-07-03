using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models
{
	
	public class EchoTest : JsonModel
	{
		public string test { get; set; }

		public static void Receive (string json)
		{
			EchoTest echoTest = JsonConvert.DeserializeObject < EchoTest > (json);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}