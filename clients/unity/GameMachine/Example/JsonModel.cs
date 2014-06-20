using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using JsonEntity = GameMachine.Messages.JsonEntity;

namespace GameMachine.Example
{
    
    public class JsonModel
    {
        private static Dictionary<string, Received> callbacks = new Dictionary<string, Received>();
        public delegate void Received(object message);

        public static void RegisterCallback(string klass, Received callback)
        {
            callbacks [klass] = callback;
        }

        public static void OnReceive(string json)
        {

        }
    }
}