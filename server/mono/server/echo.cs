using System;
using System.Text;
using System.IO;
using System.Collections.Generic;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMachine;

namespace MyGame
{
    public class Echo : GameMachine.Callable
    {
        public GameMessage call(GameMessage message)
        {
            return message;
        }
    }
}
