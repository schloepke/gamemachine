using UnityEngine;
using GameMachine;

public class DeadLetters : UntypedActor
{
    public override void OnReceive(object message)
    {
        Debug.Log("DeadLetters received message: " + message);
    }
}

