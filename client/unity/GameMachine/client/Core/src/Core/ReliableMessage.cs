
using System;
using GameMessage = io.gamemachine.messages.GameMessage;

namespace GameMachine.Core
{
    public class ReliableMessage
    {
        public double lastUpdate;
        public GameMessage gameMessage;
        public int attempts = 0;
        public int maxAttempts = 5;

        public ReliableMessage (GameMessage gameMessage)
        {
            this.gameMessage = gameMessage;
            lastUpdate = currentTime ();
        }

        public double currentTime ()
        {
            DateTime baseDate = new DateTime (1970, 1, 1);
            TimeSpan diff = DateTime.Now - baseDate;
            return diff.TotalMilliseconds;
        }
    }
}

