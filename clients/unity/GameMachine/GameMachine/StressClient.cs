using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using EchoTest = GameMachine.Messages.EchoTest;
using Player = GameMachine.Messages.Player;
using TrackEntity = GameMachine.Messages.TrackEntity;
using Neighbors = GameMachine.Messages.Neighbors;
using GetNeighbors = GameMachine.Messages.GetNeighbors;
using TrackExtra = GameMachine.Messages.TrackExtra;

namespace GameMachine
{
    public class StressClient : MonoBehaviour
    {
        
        public Client client;
        public string username;
        public string authtoken = "stresstest";
        public bool started = false;

        private double lastUpdate = 0;
        private double updatesPerSecond = 10;
        private double updateInterval;
        private double messageSentAt;
        private GameMachine.Messages.Vector3 entityVector;

        private List<double> times = new List<double>();

        void Start()
        {

        }

        public void StartClient(string username)
        {
            entityVector = new GameMachine.Messages.Vector3();
            Random random = new Random();
            entityVector.x = Random.Range(5f, 1000f);
            entityVector.y = Random.Range(5f, 20f);
            entityVector.z = Random.Range(5f, 1000f);
            Logger.Debug(entityVector.x + " " + entityVector.y + " " + entityVector.z);

            client = new Client(username, authtoken, true);
            this.username = username;
            started = true;
            SendTimed();
            InvokeRepeating("UpdateMessage", 2, 0.06F);

        }

        void UpdateMessage()
        {
            if (started)
            {
                Entity entity = new Entity();
                   
                if (Client.entityQueue.TryDequeue(out entity))
                {
                    double messageReceivedAt = Time.realtimeSinceStartup - messageSentAt;
                    times.Add(messageReceivedAt);
                    SendTimed();
                }

                if (times.Count >= 50)
                {
                    double average = Mean(times);
                    Logger.Debug(average.ToString());
                    times.Clear();
                }

            }

        }

        void OnApplicationQuit()
        {
            client.Stop();
        }

        void SendTimed()
        {
            messageSentAt = Time.realtimeSinceStartup;
            SendMessage();
        }

        void SendMessage()
        {
            //EchoTest();
            Track();
        }

        public void Track()
        {
            Entity entity = new Entity();
            entity.id = username;
            entity.entityType = "player";
            
            entity.vector3 = entityVector;

            
            TrackEntity trackEntity = new TrackEntity();
            trackEntity.value = true;
            entity.trackEntity = trackEntity;
            GetNeighbors getNeighbors = new GetNeighbors();
            getNeighbors.vector3 = entity.vector3;

            entity.getNeighbors = getNeighbors;
            client.SendEntity(entity); 
        }
        public void Devnull()
        {
            Entity entity = new Entity();
            entity.id = "test";
            entity.destination = "GameMachine/GameSystems/Devnull";
            client.SendEntity(entity); 
        }

        public void StressMsg()
        {
            Entity entity = new Entity();
            entity.id = "test";
            entity.destination = "GameMachine/GameSystems/StressTest";
            client.SendEntity(entity); 
        }

        public void EchoTest()
        {
            EchoTest test = new EchoTest();
            test.message = "test";
            Entity entity = new Entity();
            entity.id = "test";
            entity.echoTest = test;
            entity.destination = "GameMachine/GameSystems/RemoteEcho";
            client.SendEntity(entity); 
        }

        public double Mean(List<double> values)
        {
            return values.Count == 0 ? 0 : Mean(values, 0, values.Count);
        }
        
        public double Mean(List<double> values, int start, int end)
        {
            double s = 0;
            
            for (int i = start; i < end; i++)
            {
                s += values [i];
            }
            
            return s / (end - start);
        }
        
    }
}