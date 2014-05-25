using System;
using System.Text;
using System.IO;
using  ProtoBuf;
using Entity = com.game_machine.entity_system.generated.Entity;

namespace GameMachine
{
	public class MessageUtil
	{
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
		
	}
}