using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Runtime.InteropServices;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using RestSharp;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageEnvelope = com.game_machine.entity_system.generated.MessageEnvelope;
using Rpc = com.game_machine.entity_system.generated.Rpc;
using Neighbors = com.game_machine.entity_system.generated.Neighbors;
using GameMachine;
using System.Collections.Concurrent;

namespace GameMachine
{
	public abstract class Actor : IActor
	{
		private int udpPort = 4000;
		private string udpHost = "127.0.0.1";
		private UdpClient udpClient;
		private IPEndPoint remote;
		private static Mutex mut = new Mutex ();
		public static ConcurrentDictionary<string,IActor> actors = new ConcurrentDictionary<string,IActor> ();
				
		public Actor ()
		{
			remote = new IPEndPoint (IPAddress.Parse (udpHost), udpPort);
			//udpClient = new UdpClient ();
			//udpClient.Connect (remote);
			//udpClient.Client.ReceiveTimeout = 20;
			
			Console.WriteLine ("Actor.new");
		}
		
		public static void ReceiveMessage (string id, string name_space, string klass, string str)
		{
			try {
				byte[] bytes = Convert.FromBase64String(str);
				//byte[] bytes = System.Text.Encoding.ASCII.GetBytes (str);
				//byte[] bytes = new byte[str.Length * sizeof(char)];
				//System.Buffer.BlockCopy (str.ToCharArray (), 0, bytes, 0, bytes.Length);
				int len = Buffer.ByteLength (bytes);
				//Console.WriteLine ("bytes len= "+len+" str len= "+str.Length);
				//Console.WriteLine (str);
				IActor actor;
				Entity entity = Actor.ByteArrayToEntity (bytes);
				if (Actor.actors.TryGetValue (id, out actor)) {
					actor.OnReceive (entity);
				} else {
					string typeName = name_space + "." + klass;
					Type type = Type.GetType (typeName);
					if (type == null) {
						Console.WriteLine (typeName + " is null");
						return;
					}
					actor = Activator.CreateInstance (type) as IActor;
					if (Actor.actors.TryAdd (id, actor)) {
						actor.OnReceive (entity);
					} else {
						Console.WriteLine ("Unable to add actor " + id);
					}
				}
			} catch (Exception ex) {
				Console.WriteLine (ex.Message);
			}
		}
		
		void Tell (string server, string name, string id, string type, Entity entity)
		{
			MessageEnvelope messageEnvelope = new MessageEnvelope ();
			messageEnvelope.name = name;
			entity.messageEnvelope = messageEnvelope;
			
			if (type == "r") {
				messageEnvelope.type = "r";
				messageEnvelope.server = server;
			} else if (type == "dl") {
				messageEnvelope.type = "dl";
				messageEnvelope.id = id;
			} else if (type == "d") {
				messageEnvelope.type = "d";
				messageEnvelope.id = id;
			} else if (type == "l") {
				messageEnvelope.type = "l";
			} else {
				throw new System.ArgumentException ("type is null or invalid", type);
			}
			
			byte[] bytes = EntityToByteArray (entity);
			udpClient.Send (bytes, bytes.Length);
		}
		
		public void Tell (string name, Entity entity)
		{
			Tell (null, name, null, "l", entity);
		}
		
		public void TellRemote (string server, string name, Entity entity)
		{
			Tell (server, name, null, "r", entity);
		}
		
		public void TellDistributedLocal (string id, string name, Entity entity)
		{
			Tell (null, name, id, "dl", entity);
		}
		
		public void TellDistributed (string id, string name, Entity entity)
		{
			Tell (null, name, id, "d", entity);
		}
		
		public Neighbors GetNeighbors (float x, float z, string entityType="player")
		{
			try {
				Entity entity = new Entity ();
				entity.id = "0";
				entity.rpc = new Rpc ();
				entity.rpc.method = "neighbors";
				entity.rpc.arguments.Add (x.ToString ("N4"));
				entity.rpc.arguments.Add (z.ToString ("N4"));
				entity.rpc.arguments.Add (entityType);
				entity.rpc.returnValue = true;
				byte[] bytes = EntityToByteArray (entity);
				udpClient.Send (bytes, bytes.Length);
				bytes = udpClient.Receive (ref remote);
				entity = ByteArrayToEntity (bytes);
				return entity.neighbors;
			} catch (Exception ex) {
				Console.WriteLine (ex.Message);
				return null;
			}
		}
		
		public static Entity ByteArrayToEntity (byte[] bytes)
		{
			Entity entity;
			MemoryStream stream = new MemoryStream (bytes);
			entity = Serializer.Deserialize<Entity> (stream);
			return entity;
		}
		
		public static byte[] EntityToByteArray (Entity entity)
		{
			byte[] data;
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, entity);
			data = stream.ToArray ();
			return data;
		}
		
		public abstract void OnReceive (object message);
		
	}
}
