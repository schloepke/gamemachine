using System.Collections;
using System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;
using EchoTest = GameMachine.Messages.EchoTest;
using Player = GameMachine.Messages.Player;
using TrackEntity = GameMachine.Messages.TrackEntity;
using Neighbors = GameMachine.Messages.Neighbors;
using GetNeighbors = GameMachine.Messages.GetNeighbors;
using TrackData = GameMachine.Messages.TrackData;

namespace GameMachine
{
    public class StressClient2
    {
		
        public Client client;
        public string username;
        public string authtoken = "stresstest";
        public bool started = false;
		
        private double messageSentAt;
        private GameMachine.Messages.Vector3 entityVector;
        private List<double> times = new List<double> ();
        private string authUri;
		
        public string udpHost = "192.168.1.8";
        public int udpPort = 24130;
        public int udpRegionPort;
        private Entity entity;


        public StressClient2 (string username)
        {
            entity = new Entity ();
            entityVector = new GameMachine.Messages.Vector3 ();
            entityVector.x = 500f;
            entityVector.y = 500f;
            entityVector.z = 500f;

            client = new AsyncUdpClient (udpHost, udpPort, username, authtoken, true);
            //client = new AsyncTcpClient (udpHost, 8910, username, authtoken);
            client.Start ();
            this.username = username;
        }

        public void ReadMessage ()
        {
            Entity entity = new Entity ();
            if (ClientMessageQueue.entityQueue.TryDequeue (out entity)) {
            }
        }
				
        public void SendMessage ()
        {
            EchoTest ();
            //Track ();
        }
		
        public void Track ()
        {
            Entity entity = new Entity ();
            entity.id = username;
            entity.entityType = "player";
			
            entity.vector3 = entityVector;
			
			
            TrackEntity trackEntity = new TrackEntity ();
            trackEntity.value = true;
            entity.trackEntity = trackEntity;
            GetNeighbors getNeighbors = new GetNeighbors ();
            getNeighbors.vector3 = entity.vector3;
			
            entity.getNeighbors = getNeighbors;
            entity.fastpath = true;
            client.SendEntity (entity); 
        }
        public void Devnull ()
        {
            Entity entity = new Entity ();
            entity.id = "test";
            entity.destination = "GameMachine/GameSystems/Devnull";
            client.SendEntity (entity); 
        }
		
        public void StressMsg ()
        {
            Entity entity = new Entity ();
            entity.id = "test";
            entity.destination = "GameMachine/GameSystems/StressTest";
            client.SendEntity (entity); 
        }
		
        public void EchoTest ()
        {
            EchoTest test = new EchoTest ();
            test.message = "test";
            Entity entity = new Entity ();
            entity.id = "test";
            entity.echoTest = test;
            entity.destination = "GameMachine/GameSystems/RemoteEcho";
            client.SendEntity (entity); 
        }
		
        public double Mean (List<double> values)
        {
            return values.Count == 0 ? 0 : Mean (values, 0, values.Count);
        }
		
        public double Mean (List<double> values, int start, int end)
        {
            double s = 0;
			
            for (int i = start; i < end; i++) {
                s += values [i];
            }
			
            return s / (end - start);
        }
		
    }
}