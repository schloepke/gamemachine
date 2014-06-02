using  ProtoBuf;
using Entity = GameMachine.Messages.Entity;
using GameMachine;
using  System.Text.RegularExpressions;
using System;

public class RemoteActorRef : UntypedActor
{
    private string destination;

    public RemoteActorRef(string _destination)
    {
        destination = _destination;
    }

    public override void OnReceive(object message)
    {
        Entity entity = message as Entity;
        if (!String.IsNullOrEmpty(destination) && destination != "default")
        {
            entity.destination = destination;
        }

        actorSystem.TellRemote(entity);
    }
}