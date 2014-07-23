using System;
using System.Text;
using System.IO;
using System.Collections.Generic;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMachine;

namespace MyGame
{
    public class TestCallable : GameMachine.Callable
    {
			
        public TestCallable()
        {
        }
		
        public GameMessage call(GameMessage message)
        {
            return message;
        }
    }
}
