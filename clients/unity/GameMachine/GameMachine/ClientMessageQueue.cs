using System;
using System.Collections.Concurrent;
using GameMachine;
using Entity = GameMachine.Messages.Entity;


namespace GameMachine
{
    public class ClientMessageQueue
    {
        public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity>();
    }
}