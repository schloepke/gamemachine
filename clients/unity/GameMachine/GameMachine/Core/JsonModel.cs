using System.Collections;
using System;
using  System.Collections.Generic;
using System.Reflection;
using GameMachine;
using GameMachine.Models;
using Entity = io.gamemachine.messages.Entity;
using JsonEntity = io.gamemachine.messages.JsonEntity;
using System.Diagnostics;
using Newtonsoft.Json;

namespace GameMachine.Core
{
    
    public abstract class JsonModel
    {
        public string id { get; set; }
        public string klass { get; set; }

        // We default to regional as json models are almost entirely userland, which means
        // they will be regional/single server and not cluster wide services.
        private bool isRegional = true;

        public abstract string ToJson ();

        public static Dictionary<string, Type> models = new Dictionary<string, Type> ();
        public static Dictionary<string, string> localToRemote = new Dictionary<string, string> ();
        public static Dictionary<string, string> destinations = new Dictionary<string, string> ();

        public void SetRegional (bool regional)
        {
            this.isRegional = regional;
        }

        public static void Register (Type t, string remoteClass)
        {
            models [remoteClass] = t;
            localToRemote [t.Name] = remoteClass;
        }

        public static void Register (Type t, string remoteClass, string destination)
        {
            models [remoteClass] = t;
            localToRemote [t.Name] = remoteClass;
            destinations [t.Name] = destination;

        }
        
        public static void OnReceive (string remoteClass, string json)
        {
            if (models.ContainsKey (remoteClass)) {
                Type t = models [remoteClass];
                MethodInfo method = t.GetMethod ("Receive");
                string[] args = {json};
                method.Invoke (null, args);
            }

        }

        public static Entity ToJsonEntity (JsonModel model)
        {
            string remoteClass = model.GetType ().Name;
            model.klass = localToRemote [remoteClass];

            return AsJsonEntity (model.ToJson (), model.klass);
        }

        public static Entity AsJsonEntity (string json, string klass)
        {
            Entity entity = new Entity ();
            entity.id = "j";
            JsonEntity jsonEntity = new JsonEntity ();
            jsonEntity.json = json;
            jsonEntity.klass = klass;
            entity.jsonEntity = jsonEntity;
            return entity;
        }

        // Saves the model on the server
        public void Save ()
        {
            string destination = "GameMachine/GameSystems/JsonModelPersistence";
            Send (destination);
        }

        // Finds a by id on the server and returns it
        public static void Find (string id, Type type)
        {
            JsonFind model = new JsonFind ();
            model.id = "find_by_id" + id;
            string destination = "GameMachine/GameSystems/JsonModelPersistence";

            string remoteClass = type.Name;
            model.klass = localToRemote [remoteClass];

            string json = JsonConvert.SerializeObject (model, Formatting.Indented);
            Entity entity = AsJsonEntity (json, model.klass);

            ActorSystem.Instance.FindRemote (destination).Tell (entity);
        }

		
        public void Send ()
        {
            string destination = destinations [this.GetType ().Name];
            Send (destination);
        }

        public void Send (string destination)
        {
            string remoteClass = this.GetType ().Name;
            this.klass = localToRemote [remoteClass];
            Entity entity = AsJsonEntity (ToJson (), this.klass);

            if (isRegional) {
                ActorSystem.Instance.FindRegional (destination).Tell (entity);
            } else {
                ActorSystem.Instance.FindRemote (destination).Tell (entity);
            }

        }

    }
}