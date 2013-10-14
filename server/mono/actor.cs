using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Runtime.InteropServices;
using System.Text;
using System.IO;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageEnvelope = com.game_machine.entity_system.generated.MessageEnvelope;
using Rpc = com.game_machine.entity_system.generated.Rpc;
using Neighbors = com.game_machine.entity_system.generated.Neighbors;

namespace GameMachine
{
	public abstract class Actor
	{
		private int udpPort = 4000;
		private string udpHost = "127.0.0.1";
		private UdpClient udpClient;
		private IPEndPoint remote;
				
		public Actor ()
		{
			remote = new IPEndPoint (IPAddress.Parse (udpHost), udpPort);
			udpClient = new UdpClient ();
			udpClient.Connect (remote);
			udpClient.Client.ReceiveTimeout = 20; 
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
			
			byte[] bytes = EntityToByteArray(entity);
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
				byte[] bytes = EntityToByteArray(entity);
				udpClient.Send (bytes, bytes.Length);
				bytes = udpClient.Receive (ref remote);
				entity = ByteArrayToEntity (bytes);
				return entity.neighbors;
			} catch (Exception ex) {
				Console.WriteLine (ex.Message);
				return null;
			}
		}
		
		public Entity ByteArrayToEntity(byte[] bytes) {
			MemoryStream stream = new MemoryStream (bytes);
			Entity entity = Serializer.Deserialize<Entity> (stream);
			return entity;
		}
		
		public byte[] EntityToByteArray (Entity entity)
		{
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, entity);
			return stream.ToArray ();
		}
		
		public abstract void OnReceive (byte[] bytes);
		
	}
}
