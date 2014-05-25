using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageRouting = com.game_machine.entity_system.generated.MessageRouting;
using GameMachine;
using System.Collections.Concurrent;

namespace GameMachine
{
	public abstract class Actor : IActor
	{
		private ProxyClient proxyClient;
		private MessageRouting messageRouting;

		public Actor ()
		{
		}

		public void PostInit(ProxyClient pc)
		{
			proxyClient = pc;
			messageRouting = new MessageRouting();
		}

		public abstract void OnReceive (object message);

		public void Tell(string destination,Entity entity)
		{
			messageRouting.destination = destination;
			entity.messageRouting = messageRouting;
			byte[] bytes = MessageUtil.EntityToByteArray (entity);
			proxyClient.Send(bytes);
		}
	}
}
