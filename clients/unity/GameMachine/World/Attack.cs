using System.Collections;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Newtonsoft.Json;

// Json models are an alternative to protocol buffers.  They are slower and larger, but much
// simpler to work with.  The server side has a corresponding json model.


// Register our json models.  Register takes the typeof of our model, the fully qualified
// classname of the server model, and the route to the server side actor we will send this to.
//JsonModel.Register(typeof(Attack), "Example::Models::Attack", "Example/CombatController");

// Create an Attack and send it
//Attack attack = new Attack();
//attack.attacker = "test";
//attack.target = "test";
//attack.combat_ability = "test_attack";
//attack.Send();

// Alternatively you can call Register without the actor path, and then call Send with the path,
// like so:
// JsonModel.Register(typeof(Attack), "Example::Models::Attack");
// attack.Send("Example/CombatController");

namespace GameMachine.World
{
    
	public class Attack : JsonModel
	{
		// Model properties
		// properties of server and client models must have the same case!
		// Feel free to choose whatever case you like, we follow the server
		// naming here which is underscored.
		public string target { get; set; }
		public string attacker { get; set; }
		public string combat_ability { get; set; }


		// This is where incoming json messages go.  Deserialize and then do what
		// you want with it.
		public static void Receive (string json)
		{
			Attack attack = JsonConvert.DeserializeObject < Attack > (json);
		}

		// Serialize your model here
		public override string ToJson ()
		{
			return JsonConvert.SerializeObject (this, Formatting.Indented);
		}
	}
}