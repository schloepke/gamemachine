using System;
using  ProtoBuf;
using System.Text;
using System.IO;
using Entity = com.game_machine.entity_system.generated.Entity;
using Neighbors = com.game_machine.entity_system.generated.Neighbors;
using GameMachine;

namespace GameMachine
{
	public class TestActor : GameMachine.Actor
	{
	
		TestActor ()
		{
		}
	
		public override void OnReceive (byte[] bytes)
		{
			Entity entity = ByteArrayToEntity (bytes);
			Tell ("GameMachine::GameSystems::LocalEcho", entity);
			Neighbors neighbors = GetNeighbors (1.0f, 1.0f);
			foreach (Entity npc in neighbors.npc) {
			}
			//throw new System.ArgumentException("test exception please ignore", "");
			//Console.WriteLine(System.Text.Encoding.Default.GetString(bytes));
		}
	}
}