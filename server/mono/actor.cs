using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Runtime.InteropServices;
using System.Text;
using System.IO;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageEnvelope = com.game_machine.entity_system.generated.MessageEnvelope;

namespace GameMachine
{
	class Actor
	{
		private UdpClient udpClient;
				
		Actor ()
		{
			udpClient = new UdpClient ();
			udpClient.Connect ("localhost", 4000);
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
				throw new System.ArgumentException("type is null or invalid", type);
			}
			
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, entity);
			byte[] bytes = stream.ToArray ();
			udpClient.Send (bytes, bytes.Length);
		}
		
		void Tell (string name, Entity entity)
		{
			Tell (null, name, null, "l", entity);
		}
		
		void TellRemote (string server, string name, Entity entity)
		{
			Tell (server, name, null, "r", entity);
		}
		
		void TellDistributedLocal (string id, string name, Entity entity)
		{
			Tell (null, name, id, "dl", entity);
		}
		
		void TellDistributed (string id, string name, Entity entity)
		{
			Tell (null, name, id, "d", entity);
		}
		
		void OnReceive (byte[] bytes)
		{
			//Console.Out.WriteLine("onReceive called");
			MemoryStream stream = new MemoryStream (bytes);
			Entity entity = Serializer.Deserialize<Entity> (stream);
			Tell ("GameMachine::GameSystems::LocalEcho", entity);
			//Console.WriteLine(System.Text.Encoding.Default.GetString(bytes));
		}

	}
}
