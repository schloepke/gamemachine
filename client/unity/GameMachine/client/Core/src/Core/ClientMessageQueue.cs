using System;
using System.Collections.Concurrent;
using GameMachine;
using Entity = io.gamemachine.messages.Entity;
using RpcMessage = io.gamemachine.messages.RpcMessage;

namespace GameMachine.Core
{
	public class ClientMessageQueue
	{
		public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity> ();
        public static ConcurrentQueue<RpcMessage> rpcQueue = new ConcurrentQueue<RpcMessage>();
	}
}