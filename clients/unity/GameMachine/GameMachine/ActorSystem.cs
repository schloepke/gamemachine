using System;
using System.Reflection;
using System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using JsonEntity = GameMachine.Messages.JsonEntity;
using Newtonsoft.Json;
using System.IO;
using ProtoBuf;
using UnityEngine;

namespace GameMachine
{
    public sealed class ActorSystem
    {
        private Client client;
        private Client regionClient;
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

        public void SetRegionClient(Client client)
        {
            this.regionClient = client;
        }

        public void RegisterActor(UntypedActor actor)
        {
            actor.SetActorSystem(this);
            string name = actor.GetType().Name;
            actors.Add(name, actor);
        }

        public UntypedActor FindRemote(string name)
        {
            return Find(name, false, true);
        }


        public UntypedActor FindRegional(string name)
        {
            if (RegionClient.connected)
            {
                return Find(name, true, true);
            } else
            {
                return Find(name, false, true);
            }
        }

        public UntypedActor Find(string name)
        {
            return Find(name, false, false);
        }

        public UntypedActor Find(string name, bool regional, bool remote)
        {
            if (actors.ContainsKey(name))
            {
                return actors [name];
            } else if (remote)
            {
                RemoteActorRef remoteActorRef = new RemoteActorRef(name);
                remoteActorRef.SetActorSystem(this);
                remoteActorRef.SetRegional(regional);
                return remoteActorRef;
            } else
            {
                return actors ["DeadLetters"];
            }
        }

        public void TellRemote(Entity entity)
        {
            client.SendEntity(entity);
        }

        public void TellRemoteRegion(Entity entity)
        {
            if (regionClient == null)
            {
                Logger.Debug("No region client set!");
            } else
            {
                regionClient.SendEntity(entity);
            }
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

        public void Update()
        {
            DeliverQueuedMessages();
        }

        public void DeliverQueuedMessages()
        {
            Entity entity = new Entity();
            
            for (int i = 0; i < 10; i++)
            {
                if (ClientMessageQueue.entityQueue.TryDequeue(out entity))
                {
                    if (entity.neighbors != null)
                    {
                        EntityTracking entityTracking = actors ["EntityTracking"] as EntityTracking;
                        entityTracking.OnReceive(entity);
                        continue;
                    }
                    // See if we have a json entity
                    if (entity.jsonEntity != null)
                    {
                        JsonModel.OnReceive(entity.jsonEntity.klass, entity.jsonEntity.json);
                        continue;
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
                                //Logger.Debug("Found component " + component);
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