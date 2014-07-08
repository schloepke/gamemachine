using System.Collections;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
	public class StressTool
	{
		public List<StressClient2> clients = new List<StressClient2> ();
		
		void Start ()
		{
			Test1 ();
			//Invoke ("RunClients", 0.05F);
			
		}

		
		private void StopClients ()
		{
			foreach (StressClient2 client in clients) {
				client.client.Stop ();
			}
		}
		
		private void RunClients ()
		{
			foreach (StressClient2 client in clients) {
				client.SendMessage ();
				client.ReadMessage ();
			}
			Thread.Sleep (20);
		}
		
		private void Test1 ()
		{
			int count = 100;
			string username;
			
			for (int i = 0; i < count; i++) {
				username = "player_" + i;
				StressClient2 client = new StressClient2 (username);
				clients.Add (client);
			}
			
		}

		
	}
}
