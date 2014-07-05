using System.Collections;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
	public class StressTest : MonoBehaviour
	{
		public List<StressClient2> clients = new List<StressClient2> ();

		void Start ()
		{
			Application.runInBackground = true;
			Test1 ();
			//Invoke ("RunClients", 0.05F);

		}

		private void Test2 ()
		{
			int count = 50;
			string username;
			
			for (int i = 0; i < count; i++) {
				username = "player_" + i;
				AddClient (username);
				//Task task = new Task(() => AddThreadedClient(username));
				//task.Start();
			}
		}

		void OnApplicationQuit ()
		{
			StopClients ();
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
			Invoke ("RunClients", 0.01F);
		}

		private void Test1 ()
		{
			int count = 1000;
			string username;
			
			for (int i = 0; i < count; i++) {
				username = "player_" + i;
				StressClient2 client = new StressClient2 (username);
				clients.Add (client);
			}

		}

		void AddThreadedClient (string username)
		{
			ThreadedStressClient client = new ThreadedStressClient ();
			client.Start (username);
		}

		void AddClient (string username)
		{
			GameObject clientGameObject = new GameObject (username);
			clientGameObject.transform.parent = this.gameObject.transform;
			StressClient stressClient = clientGameObject.AddComponent ("StressClient") as StressClient;
			stressClient.StartClient (username);
		}
       
	}
}
