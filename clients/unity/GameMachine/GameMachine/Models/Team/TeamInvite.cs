using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

namespace GameMachine.Models.Team
{
	
	public class TeamInvite : JsonModel
	{
		public string name { get; set; }
		public string invite_id { get; set; }
		public string invitee { get; set; }

		public static void Receive (string json)
		{
			TeamInvite teamInvite = JsonConvert.DeserializeObject < TeamInvite > (json);
			TeamsManager.Instance.inviteReceived (teamInvite);
		}
		
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}