using System;
using System.Reflection;
using System.Collections.Generic;
using UnityEngine;
using io.gamemachine.messages;
using GameMachine.Common;

namespace GameMachine.Core {
    public class ActorSystem : MonoBehaviour {
        public static bool sendImmediate = true;
        public Client client;
        private Client regionClient;
        private Dictionary<string, UntypedActor> actors = new Dictionary<string, UntypedActor>();
        private Dictionary<string, MethodInfo> methods = new Dictionary<string, MethodInfo>();
        private List<Entity> entities = new List<Entity>();
        private List<Entity> regionEntities = new List<Entity>();

        public bool running = false;

        public static ActorSystem instance;

        void Awake() {
            instance = this;
        }

        public void Activate(Client _client) {
            client = _client;
            DeadLetters deadletters = new DeadLetters();
            RegisterActor(deadletters);
            CreateMethodCache();
            running = true;
        }

        public void SetRegionClient(Client client) {
            this.regionClient = client;
        }

        public void RegisterActor(UntypedActor actor) {
            actor.SetActorSystem(this);
            string name = actor.GetType().Name;
            if (actors.ContainsKey(name)) {
                //Debug.Log("Actor " + name + " already registered");
                actors.Remove(name);
            }
            actors.Add(name, actor);
        }

        public UntypedActor FindRemote(string name) {
            return Find(name, false, true);
        }


        public UntypedActor FindRegional(string name) {
            if (RegionClient.connected) {
                return Find(name, true, true);
            } else {
                return Find(name, false, true);
            }
        }

        public UntypedActor Find(string name) {
            return Find(name, false, false);
        }

        public UntypedActor Find(string name, bool regional, bool remote) {
            if (actors.ContainsKey(name)) {
                return actors[name];
            } else if (remote) {
                RemoteActorRef remoteActorRef = new RemoteActorRef(name);
                remoteActorRef.SetActorSystem(this);
                return remoteActorRef;
            } else {
                return actors["DeadLetters"];
            }
        }

        public void TellRemote(Entity entity) {
            if (sendImmediate) {
                if (client != null) {
                    client.SendEntity(entity);
                }
                return;
            }

            entities.Add(entity);
        }


        private void CreateMethodCache() {
            Entity entity = new Entity();
            foreach (PropertyInfo info in entity.GetType().GetProperties()) {
                MethodInfo minfo = info.GetGetMethod();
                string name = info.Name;
                if (methods.ContainsKey(name)) {
                    //Debug.Log("Method " + name + " already registered");
                    methods.Remove(name);
                }
                methods.Add(name, minfo);

            }
        }

        public void AppUpdate(bool connected) {
            
            if (NpcManager.instance != null) {
                NpcManager.instance.UpdateTracking();
                GameEntityManager.instance.UpdateTracking(false);
            } else {
                if (GameEntityManager.instance != null) {
                    GameEntityManager.instance.UpdateTracking(true);
                }
            }

            


            if (regionEntities.Count >= 1) {
                regionClient.SendEntities(regionEntities);
                regionEntities.Clear();
            }
            if (entities.Count >= 1) {
                client.SendEntities(entities);
                entities.Clear();
            }
        }

        void Update() {
            DeliverQueuedMessages();
        }

        private void DeliverQueuedMessages() {
            Entity entity;

            for (int i = 0; i < ClientMessageQueue.entityQueue.Count; i++) {
                if (ClientMessageQueue.entityQueue.TryDequeue(out entity)) {

                    bool trackDataFound = false;
                    if (entity.trackDataResponse != null) {
                        GameEntityManager.instance.HandleTrackDataResponse(entity.trackDataResponse);
                        trackDataFound = true;
                    }

                    if (entity.neighbors != null) {
                        GameEntityManager.instance.TrackDataReceived(entity.neighbors.trackData);
                        trackDataFound = true;
                    }
                    if (trackDataFound) {
                        continue;
                    }


                    if (entity.gameMessages != null) {
                        GameMessageHandler.instance.DeliverMessages(entity.gameMessages);
                        continue;
                    }


                    // entities with a destination get routed directly
                    if (!String.IsNullOrEmpty(entity.destination)) {
                        DeliverByDestination(entity);
                    } else {
                        DeliverByComponent(entity);
                    }

                } else {
                    break;
                }
            }
        }

        public void DeliverByDestination(Entity entity) {
            UntypedActor actor = Find(entity.destination);
            if (actor.GetType().Name == "DeadLetters") {
                Debug.Log("Incoming entity has invalid destination: " + entity.destination);
            } else {
                actor.Tell(entity);
            }
        }

        public void DeliverByComponent(Entity entity) {
            MethodInfo method;
            bool findFailed;

            foreach (UntypedActor actor in actors.Values) {

                List<List<string>> componentSets = actor.GetComponentSets();
                foreach (List<string> componentSet in componentSets) {

                    // See if message contains component set
                    findFailed = false;
                    foreach (string component in componentSet) {
                        if (methods.ContainsKey(component)) {
                            method = methods[component];

                            if (method.Invoke(entity, null) == null) {
                                findFailed = true;

                            } else {
                                //Logger.Debug("Found component " + component);
                            }
                        } else {
                            Debug.Log("Bad Component " + component);
                        }

                    }

                    if (!findFailed) {
                        actor.Tell(entity);
                        findFailed = false;
                        break;
                    }
                }
            }
        }

    }
}