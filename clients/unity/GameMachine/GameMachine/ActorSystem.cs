using System;
using System.Reflection;
using System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
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

        public void TellRemote(string destination, Entity entity)
        {
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

        public int DeliverQueuedMessages()
        {
            int count = 0;

            Entity entity = new Entity();
            MethodInfo method;
            bool findFailed;
            for (int i = 0; i < 10; i++)
            {
                if (client.entityQueue.TryDequeue(out entity))
                {
                    Logger.Debug("Processing entity");
                    foreach (UntypedActor actor in actors.Values)
                    {
                        Logger.Debug("Processing actor " + actor.GetType().Name);

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

                                    Logger.Debug("Looking for component " + component);
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
                                count++;
                                break;
                            }
                        }
                    }
                } else
                {
                    break;
                }
            }
            return count;
        }

    }
}