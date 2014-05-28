using  ProtoBuf;
using UnityEngine;
using Entity = GameMachine.Messages.Entity;
using GameMachine;

namespace Example
{
    public class TestActor : UntypedActor
    {
        public static string[] aspect = { "Player", "Test", "Test2" };
		
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            Debug.Log("Test actor received message");

        }
    }
}
