using System;
using System.Text;
using System.IO;
using System.Threading;
using System.Runtime.InteropServices;
using  ProtoBuf;
using System.Collections.Generic;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageEnvelope = com.game_machine.entity_system.generated.MessageEnvelope;
using Rpc = com.game_machine.entity_system.generated.Rpc;
using Neighbors = com.game_machine.entity_system.generated.Neighbors;
using GameMachine;

namespace GameMachine
{
	public class TestActor : GameMachine.Actor
	{
			
		public TestActor ()
		{
		}
		
		public override void OnReceive (object message)
		{
			try {
				Entity entity = message as Entity;
				Tell("GameMachine::GameSystems::Devnull",entity);
			} catch (Exception ex) {
				Console.WriteLine (ex);
			}

		}
	}
}
