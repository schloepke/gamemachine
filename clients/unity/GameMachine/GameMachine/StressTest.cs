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
            int count = 100;
            string username;
            string authtoken = "stresstest";



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
