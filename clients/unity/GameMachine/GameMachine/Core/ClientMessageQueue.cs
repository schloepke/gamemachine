using System;
using System.Collections.Concurrent;
using GameMachine;
using Entity = io.gamemachine.messages.Entity;


namespace GameMachine.Core
{
	public class ClientMessageQueue
	{
		public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity> ();
	}
}