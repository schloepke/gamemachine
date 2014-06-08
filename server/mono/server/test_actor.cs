using System;
using System.Text;
using System.IO;
using System.Threading;
using System.Runtime.InteropServices;
using  ProtoBuf;
using System.Collections.Generic;
using Entity = GameMachine.Messages.Entity;
using GameMachine;

namespace GameMachine
{
    public class TestActor : GameMachine.Actor
    {
			
        public TestActor()
        {
        }
		
        public override void OnReceive(object message)
        {
            try
            {
                Entity entity = message as Entity;
                Tell("/GameMachine/GameSystems/Devnull", entity);
            } catch (Exception ex)
            {
                ProxyServer.logger.Info(ex);
            }

        }
    }
}
