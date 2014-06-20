using System.Collections;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public class StressTest : MonoBehaviour
    {
       
        void Start()
        {
            Application.runInBackground = true;
            int count = 50;
            string username;
            string authtoken = "stresstest";
            //string host = "162.243.128.58";
            //string host = "192.168.1.8";
            string host = "127.0.0.1";
            
            GameMachine.Config.authUri = "http://" + host + ":3000/auth";
            GameMachine.Config.udpHost = host;
            //GameMachine.Config.udpHost = "127.0.0.1";
            GameMachine.Config.udpPort = 8100;



            for (int i = 0; i < count; i++)
            {
                username = "player_" + i;
                AddClient(username);
                //Task task = new Task(() => AddThreadedClient(username));
                //task.Start();
            }

        }


        void AddThreadedClient(string username)
        {
            ThreadedStressClient client = new ThreadedStressClient();
            client.Start(username);
        }

        void AddClient(string username)
        {
            GameObject clientGameObject = new GameObject(username);
            clientGameObject.transform.parent = this.gameObject.transform;
            StressClient stressClient = clientGameObject.AddComponent("StressClient") as StressClient;
            stressClient.StartClient(username);
        }
       
    }
}
