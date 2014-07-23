using System;
using System.Text;
using System.IO;
using System.Collections.Generic;
using Entity = GameMachine.Messages.Entity;
using GameMachine;

namespace MyGame
{
    public class Echo : GameMachine.Callable
    {
        public Entity call(Entity entity)
        {
            return entity;
        }
    }
}
