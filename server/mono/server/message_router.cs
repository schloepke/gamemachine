using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMachine;
using System.Collections.Concurrent;
using NLog;

namespace GameMachine
{
    public class MessageRouter
    {
        public ConcurrentDictionary<string,Callable> callables = new ConcurrentDictionary<string,Callable>();
        public static Logger logger = LogManager.GetLogger("GameMachine");
		
        public GameMessage Route(string klass, GameMessage message)
        {
            try
            {
                Callable callable;
                string typeName = klass;
                string threadId = System.Threading.Thread.CurrentThread.ManagedThreadId.ToString();
                string id = typeName + threadId;

                if (callables.TryGetValue(id, out callable))
                {
                    return callable.call(message);
                } else
                {
                    Type type = Type.GetType(typeName);
                    if (type == null)
                    {
                        MessageRouter.logger.Info(typeName + " is null");
                        return null;
                    }
                    callable = Activator.CreateInstance(type) as Callable;
                    MessageRouter.logger.Info("Callable created with id " + id);
                    if (callables.TryAdd(id, callable))
                    {
                        return callable.call(message);
                    } else
                    {
                        MessageRouter.logger.Info("Unable to add callable " + id);
                        return null;
                    }
                }
            } catch (Exception ex)
            {
                Console.WriteLine(ex);
                return null;
            }
        }

    }
}