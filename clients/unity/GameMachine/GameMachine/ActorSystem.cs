using System;
using System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public class ActorSystem
    {
        private Client client;
        private int updateCount = 0;
        private Dictionary<string, UntypedActor> actors = new Dictionary<string, UntypedActor>();

        public ActorSystem(Client _client)
        {
            client = _client;
            DeadLetters deadletters = new DeadLetters();
            RegisterActor(deadletters);
        }

        public void RegisterActor(UntypedActor actor)
        {
            actor.SetActorSystem(this);
            string name = actor.GetType().Name;
            actors.Add(name, actor);
        }

        public UntypedActor Find(string name)
        {
            if (actors.ContainsKey(name))
            {
                return actors [name];
            } else if (name.StartsWith("/remote/"))
            {
                name = name.Replace("/remote/", "");
                RemoteActorRef remoteActorRef = new RemoteActorRef(name);
                remoteActorRef.SetActorSystem(this);
                return remoteActorRef;
            } else
            {
                return actors ["DeadLetters"];
            }
        }

        public void Update(int updateFrequency)
        {
            updateCount++;
            if (updateCount >= updateFrequency)
            {
                updateCount = 0;
                DeliverQueuedMessages();
            }
        }

        public void TellRemote(string destination, Entity entity)
        {
            client.SendEntity(entity);
        }

        public int DeliverQueuedMessages()
        {
            int count = 0;

            Entity entity = new Entity();
            for (int i = 0; i < 10; i++)
            {
                if (client.entityQueue.TryDequeue(out entity))
                {
                    count++;
                }
            }
            return count;
        }
    }
}