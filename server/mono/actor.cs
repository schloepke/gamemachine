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
    public abstract class Actor : IActor
    {
        private ProxyClient proxyClient;

        public Actor()
        {
        }

        public void PostInit(ProxyClient pc)
        {
            proxyClient = pc;
        }

        public abstract void OnReceive(object message);

        public void Tell(string destination, Entity entity)
        {
            entity.destination = destination;
            byte[] bytes = MessageUtil.EntityToByteArray(entity);
            proxyClient.Send(bytes);
        }
    }
}
