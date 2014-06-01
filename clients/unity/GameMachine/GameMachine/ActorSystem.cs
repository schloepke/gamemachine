using System;
using System.Reflection;
using System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using JsonEntity = GameMachine.Messages.JsonEntity;
using Newtonsoft.Json;
using UnityEngine;

namespace GameMachine
{
    public sealed class ActorSystem
    {
        private Client client;
        private int updateCount = 0;
        private Dictionary<string, UntypedActor> actors = new Dictionary<string, UntypedActor>();
        private Dictionary<string, MethodInfo> methods = new Dictionary<string, MethodInfo>();
        public bool Running = false;

        static readonly ActorSystem _instance = new ActorSystem();
        public static ActorSystem Instance
        {
            get
            {
                return _instance;
            }
        }

        ActorSystem()
        {
        }

        public void Start(Client _client)
        {
            client = _client;
            DeadLetters deadletters = new DeadLetters();
            RegisterActor(deadletters);
            CreateMethodCache();
            Running = true;
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

        // updateFrequency is the number of frames to wait for between
        // queue checks.  
        public void Update(int updateFrequency)
        {
            updateCount++;
            if (updateCount >= updateFrequency)
            {
                updateCount = 0;
                DeliverQueuedMessages();
            }
        }

        public void TellRemote(Entity entity)
        {
            if (entity.json)
            {
                JsonEntity json = new JsonEntity();
                json.json = JsonConvert.SerializeObject(entity);
                entity.jsonEntity = json;
            }
            client.SendEntity(entity);
        }

        private void CreateMethodCache()
        {
            Entity entity = new Entity();
            foreach (PropertyInfo info in entity.GetType().GetProperties())
            {
                MethodInfo minfo = info.GetGetMethod();
                string name = info.Name;
                methods.Add(name, minfo);
            }
        }

        public void DeliverQueuedMessages()
        {
            Entity entity = new Entity();
            
            for (int i = 0; i < 10; i++)
            {
                if (client.entityQueue.TryDequeue(out entity))
                {
                    // See if we have a json entity
                    if (entity.jsonEntity != null)
                    {
                        entity = JsonConvert.DeserializeObject<Entity>(entity.jsonEntity.json);
                    }
                    
                    // entities with a destination get routed directly
                    if (!String.IsNullOrEmpty(entity.destination))
                    {
                        DeliverByDestination(entity);
                    } else
                    {
                        DeliverByComponent(entity);
                    }

                } else
                {
                    break;
                }
            }
        }

        public void DeliverByDestination(Entity entity)
        {
            UntypedActor actor = Find(entity.destination);
            if (actor.GetType().Name == "DeadLetters")
            {
                Logger.Debug("Incoming entity has invalid destination: " + entity.destination);
            } else
            {
                actor.Tell(entity);
            }
        }

        public void DeliverByComponent(Entity entity)
        {
            MethodInfo method;
            bool findFailed;

            foreach (UntypedActor actor in actors.Values)
            {
                //Logger.Debug("Processing actor " + actor.GetType().Name);
                
                List<List<string>> componentSets = actor.GetComponentSets();
                foreach (List<string> componentSet in componentSets)
                {

                    // See if message contains component set
                    findFailed = false;
                    foreach (string component in componentSet)
                    {
                        if (methods.ContainsKey(component))
                        {
                            method = methods [component];
                            
                            //Logger.Debug("Looking for component " + component);
                            if (method.Invoke(entity, null) == null)
                            {
                                findFailed = true;
                                
                            } else
                            {
                                Logger.Debug("Found component " + component);
                            }
                        } else
                        {
                            Logger.Debug("Bad Component " + component);
                        }
                        
                    }

                    if (!findFailed)
                    {
                        actor.Tell(entity);
                        findFailed = false;
                        break;
                    }
                }
            }
        }

    }
}