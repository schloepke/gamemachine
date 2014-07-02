using System.Collections;
using System;
using  System.Collections.Generic;
using System.Reflection;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using JsonEntity = GameMachine.Messages.JsonEntity;

namespace GameMachine
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

		public void Send ()
		{
			string destination = destinations [this.GetType ().Name];
			Send (destination);
		}

		public void Send (string destination)
		{
			string remoteClass = this.GetType ().Name;
			this.klass = localToRemote [remoteClass];

			Entity entity = new Entity ();
			entity.id = "";
			JsonEntity jsonEntity = new JsonEntity ();
			jsonEntity.json = ToJson ();

			entity.jsonEntity = jsonEntity;
			entity.destination = destination;

			if (isRegional) {
				ActorSystem.Instance.FindRegional (destination).Tell (entity);
			} else {
				ActorSystem.Instance.FindRemote (destination).Tell (entity);
			}

		}

	}
}