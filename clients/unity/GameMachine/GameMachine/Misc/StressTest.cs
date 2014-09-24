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
            Invoke ("RunClients", 0.05F);

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
            int count = 30;
            string username;
			
            for (int i = 0; i < count; i++) {
                username = "player_" + i;
                StressClient2 client = new StressClient2 (username);
                clients.Add (client);
            }

        }

    }
}
