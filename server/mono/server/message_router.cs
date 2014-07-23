using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using Entity = GameMachine.Messages.Entity;
using GameMachine;
using System.Collections.Concurrent;
using NLog;

namespace GameMachine
{
    public class MessageRouter
    {
        public static ConcurrentDictionary<string,Callable> callables = new ConcurrentDictionary<string,IActor>();
        public static Logger logger = LogManager.GetLogger("GameMachine");
		
        public GameMessage Route(GameMessage message)
        {
            try
            {
				
                Callable callable;
               
                string typeName = entity.destination;
                string threadId = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
                string id = typeName + threadId;

                if (MessageRouter.actors.TryGetValue(id, out actor))
                {
                    actor.PostInit(proxyClient);
                    actor.OnReceive(entity);
                } else
                {
                    Type type = Type.GetType(typeName);
                    if (type == null)
                    {
                        MessageRouter.logger.Info(typeName + " is null");
                        return;
                    }
                    actor = Activator.CreateInstance(type) as IActor;
                    if (MessageRouter.actors.TryAdd(id, actor))
                    {
                        actor.PostInit(proxyClient);
                        actor.OnReceive(entity);
                    } else
                    {
                        MessageRouter.logger.Info("Unable to add actor " + id);
                    }
                }
            } catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
        }

    }
}