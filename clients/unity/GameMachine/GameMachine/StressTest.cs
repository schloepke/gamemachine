using System.Collections;
using System.Collections.Generic;
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
            int count = 30;
            string username;
            string authtoken = "stresstest";
            GameMachine.Config.authUri = "http://192.168.1.8:3000/auth";
            GameMachine.Config.udpHost = "192.168.1.8";
            //GameMachine.Config.udpHost = "127.0.0.1";
            GameMachine.Config.udpPort = 8100;



            for (int i = 0; i < count; i++)
            {
                username = "player_" + i;
                AddClient(username);
            }

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
