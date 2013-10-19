using System;
using System.Text;
using System.IO;
using System.Threading;
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
	
		private static Mutex mut = new Mutex();
		//public static Dictionary<int,TestActor> actors = new Dictionary<int,TestActor> ();
		
		public TestActor ()
		{
		}
			
		
		
		public override void OnReceive (byte[] bytes)
		{
			//throw new System.ArgumentException("test exception please ignore", "");
			try {
				Console.WriteLine ("OnReceive");
				if (bytes.Length == 0) {
				Console.WriteLine (bytes.Length);
				}
				Entity testEntity = new Entity();
				testEntity.id = "1";
				byte[] data = EntityToByteArray(testEntity);
				testEntity = ByteArrayToEntity(data);
				//mut.WaitOne();
				//Entity entity = ByteArrayToEntity (bytes);
				//mut.ReleaseMutex();
				return;
				//Tell ("GameMachine::GameSystems::LocalEcho", entity);
				//Neighbors neighbors = GetNeighbors (1.0f, 1.0f);
			} catch (Exception ex) {
				Console.WriteLine (ex.Message);
			}
			//foreach (Entity npc in neighbors.npc) {
			//}
			//throw new System.ArgumentException("test exception please ignore", "");
			//Console.WriteLine(System.Text.Encoding.Default.GetString(bytes));
		}
	}
}