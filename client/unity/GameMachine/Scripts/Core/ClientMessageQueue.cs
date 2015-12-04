using io.gamemachine.messages;
using System.Collections.Concurrent;

namespace GameMachine.Core {
    public class ClientMessageQueue
	{
		public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity> ();
        public static ConcurrentQueue<UnityGameMessage> unityGameMessageQueue = new ConcurrentQueue<UnityGameMessage>();
	}
}