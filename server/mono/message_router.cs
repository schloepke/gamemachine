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

namespace GameMachine
{
    public class MessageRouter
    {
        public static ConcurrentDictionary<string,IActor> actors = new ConcurrentDictionary<string,IActor>();

		
        public static void Route(byte[] bytes, ProxyClient proxyClient)
        {
            try
            {
				
                IActor actor;
                Entity entity = MessageUtil.ByteArrayToEntity(bytes);

                if (entity.id == "ping")
                {
                    return;
                }

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
                        Console.WriteLine(typeName + " is null");
                        return;
                    }
                    actor = Activator.CreateInstance(type) as IActor;
                    if (MessageRouter.actors.TryAdd(id, actor))
                    {
                        actor.PostInit(proxyClient);
                        actor.OnReceive(entity);
                    } else
                    {
                        Console.WriteLine("Unable to add actor " + id);
                    }
                }
            } catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
        }

    }
}